package com.alpha.books_explorer

import android.content.Context
import com.alpha.myplatformdoor.FeatureCommand
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.FeatureResult
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Singleton
class MainAppDoor @Inject constructor(
    private val viewModel: MainAppDoorViewModel,
) : FeatureEntry() {

    private val _favList = MutableSharedFlow<FeatureCommand>()
    val favList: SharedFlow<FeatureCommand> = _favList

    private val _readingList = MutableSharedFlow<FeatureCommand>()
    val readingList: SharedFlow<FeatureCommand> = _readingList

    private val _searchList = MutableSharedFlow<FeatureCommand>()
    val searchList: SharedFlow<FeatureCommand> = _searchList

    private val _bookById = MutableSharedFlow<FeatureCommand>()
    val bookById: SharedFlow<FeatureCommand> = _bookById

    override fun handle(command: FeatureCommand): Flow<FeatureResult> = flow {
        emit(
            FeatureResult.Success(
                FeatureCommand(
                    messageName = command.messageName, payload = JsonObject()
                )
            )
        )
    }

    override fun init(context: Context) {

    }

    override fun onReceive(message: FeatureCommand) {
        if (message.messageName == "ReceivedBookByIdResponse") {
            println("Shubham: Received book response: ${message}")
            viewModel.processBookByIdResponse(message.payload)
        }
    }

    override fun publish(message: FeatureCommand): SharedFlow<FeatureCommand> {
        TODO("Not yet implemented")
    }

    override fun subscribe(
        subscription: SharedFlow<FeatureCommand>,
        featureCommand: FeatureCommand,
    ) {
        collectSubscription(subscription, featureCommand)
    }

    private fun collectSubscription(
        flow: SharedFlow<FeatureCommand>,
        command: FeatureCommand,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            if (command.messageName == "SubscribeReadingList") {
                flow.collect {
                    _readingList.emit(it)
                }
            } else if (command.messageName == "SubscribeBookById") {
                flow.collect {
                    _bookById.emit(it)
                }
            } else if (command.messageName == "SubscribeFavList") {
                flow.collect {
                    _favList.emit(it)
                }
            } else if (command.messageName == "SubscribeSearchResult") {
                flow.collect {
                    _searchList.emit(it)
                }
            } else {
                flow.collect {
                    println("Shubham: Subscription: command: $command, flow: ${it}")
                }
            }
        }
    }

    fun getSearchResult(query: String) {
        messageSender.send(
            FeatureCommand(
                messageName = "GetSearchResult",
                payload = JsonObject().apply {
                    addProperty("query", query)
                }
            )
        )
    }

    fun getFavBookList() {
        messageSender.send(
            FeatureCommand(
                messageName = "GetFavList"
            )
        )
    }

    fun getReadingList() {
        messageSender.send(
            FeatureCommand(
                messageName = "GetReadingList"
            )
        )
    }

    fun getBookById(bookId: String) {
        val payload = JsonObject().apply {
            addProperty("id", bookId)
        }
        messageSender.send(
            FeatureCommand(
                messageName = "GetBookById",
                payload = payload
            )
        )
    }
}
