package com.alpha.data

import android.content.Context
import com.alpha.data.repository.BookRepositoryImpl
import com.alpha.myplatformdoor.FeatureCommand
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.FeatureResult
import com.alpha.myplatformdoor.MessageBus
import com.google.gson.Gson
import com.google.gson.JsonArray
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
class DataDoor @Inject internal constructor(
    private val bookRepositoryImpl: BookRepositoryImpl,
) : FeatureEntry() {

    @Inject
    internal lateinit var bus: MessageBus

    private val _bookByIdFlow = MutableSharedFlow<FeatureCommand>()
    private val bookByIdFlow: SharedFlow<FeatureCommand> = _bookByIdFlow

    private val _readingList = MutableSharedFlow<FeatureCommand>()
    private val readingList: SharedFlow<FeatureCommand> = _readingList

    private val _searchBooks = MutableSharedFlow<FeatureCommand>()
    private val searchBooks: SharedFlow<FeatureCommand> = _searchBooks

    private val _favBooks = MutableSharedFlow<FeatureCommand>()
    private val favBooks: SharedFlow<FeatureCommand> = _favBooks

    override fun handle(command: FeatureCommand): Flow<FeatureResult> = flow {
        val response = JsonObject().apply {
            addProperty("status", "Domain handled")
        }

        bus.publish(FeatureCommand(messageName = "GetStudentsData", response))

        emit(
            FeatureResult.Success(
                FeatureCommand(
                    messageName = command.messageName,
                    payload = response
                )
            )
        )
    }

    override fun init(context: Context) {

    }

    override fun onReceive(message: FeatureCommand) {
        if (message.messageName == "GetBookById") {
            CoroutineScope(Dispatchers.IO).launch {
                val book = bookRepositoryImpl.getBookById(message.payload?.get("id")?.asString ?: "")
                val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
                messageSender.send(
                    FeatureCommand(
                        messageName = "ReceivedBookByIdResponse",
                        payload = jsonObject
                    )
                )
                _bookByIdFlow.emit(
                    FeatureCommand(
                        messageName = "SubscribeBookById",
                        payload = jsonObject
                    )
                )
            }
        } else if (message.messageName == "GetReadingList") {
            CoroutineScope(Dispatchers.IO).launch {
                val list = bookRepositoryImpl.getReadingListBooks()
                if (list.isEmpty()) {
                    val message = FeatureCommand(
                        messageName = "SubscribeReadingList",
                        payload = JsonObject().apply {
                            addProperty("error", "list is empty")
                        }
                    )
                    _readingList.emit(
                        message
                    )
                } else {
                    val gson = Gson()
                    val jsonArray: JsonArray = gson.toJsonTree(list).asJsonArray
                    val jsonObject = JsonObject().apply {
                        add("books", jsonArray)
                    }
                    val message = FeatureCommand(
                        messageName = "SubscribeReadingList",
                        payload = jsonObject
                    )
                    _readingList.emit(
                        message
                    )
                }
            }
        } else if (message.messageName == "GetFavList") {
            CoroutineScope(Dispatchers.IO).launch {
                val list = bookRepositoryImpl.getFavListBooks()
                if (list.isEmpty()) {
                    val message = FeatureCommand(
                        messageName = "SubscribeFavList",
                        payload = JsonObject().apply {
                            addProperty("error", "list is empty")
                        }
                    )
                    _favBooks.emit(
                        message
                    )
                } else {
                    val gson = Gson()
                    val jsonArray: JsonArray = gson.toJsonTree(list).asJsonArray
                    val jsonObject = JsonObject().apply {
                        add("books", jsonArray)
                    }
                    val message = FeatureCommand(
                        messageName = "SubscribeFavList",
                        payload = jsonObject
                    )
                    _favBooks.emit(
                        message
                    )
                }
            }
        } else if (message.messageName == "GetSearchResult") {
            CoroutineScope(Dispatchers.IO).launch {
                val books = bookRepositoryImpl.getBooks(message.payload?.get("query")?.asString ?: "")
                if (books.isEmpty()) {
                    val message = FeatureCommand(
                        messageName = "SubscribeSearchResult",
                        payload = JsonObject().apply {
                            addProperty("error", "list is empty")
                        }
                    )
                    _searchBooks.emit(
                        message
                    )
                } else {
                    val gson = Gson()
                    val jsonArray: JsonArray = gson.toJsonTree(books).asJsonArray
                    val jsonObject = JsonObject().apply {
                        add("books", jsonArray)
                    }

                    val message = FeatureCommand(
                        messageName = "SubscribeSearchResult",
                        payload = jsonObject
                    )
                    _searchBooks.emit(
                        message
                    )
                }
            }
        }
    }

    override fun publish(message: FeatureCommand): SharedFlow<FeatureCommand> {
        return if (message.messageName == "SubscribeBookById") return bookByIdFlow
        else if (message.messageName == "SubscribeReadingList") return readingList
        else if (message.messageName == "SubscribeSearchResult") return searchBooks
        else if (message.messageName == "SubscribeFavList") return favBooks
        else throw Exception("Message Not Subscribed")
    }

    override fun subscribe(
        subscription: SharedFlow<FeatureCommand>,
        featureCommand: FeatureCommand,
    ) {

    }
}
