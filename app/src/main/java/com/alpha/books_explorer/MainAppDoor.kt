package com.alpha.books_explorer

import android.content.Context
import com.alpha.books_explorer.domain.model.Book
import com.alpha.modulesDoor.messageTypes.EventType
import com.alpha.modulesDoor.DoorCommand
import com.alpha.modulesDoor.DoorEntry
import com.alpha.modulesDoor.messageTypes.MessageType
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@Singleton
class MainAppDoor @Inject constructor() : DoorEntry() {

    private val _favList = MutableSharedFlow<DoorCommand>()
    val favList: SharedFlow<DoorCommand> = _favList

    private val _readingList = MutableSharedFlow<DoorCommand>()
    val readingList: SharedFlow<DoorCommand> = _readingList

    private val _searchList = MutableSharedFlow<DoorCommand>()
    val searchList: SharedFlow<DoorCommand> = _searchList

    private val _bookById = MutableSharedFlow<DoorCommand>()
    val bookById: SharedFlow<DoorCommand> = _bookById

    private val _isBookPresentInFavList = MutableSharedFlow<DoorCommand>()
    val isBookPresentInFavList: SharedFlow<DoorCommand> = _isBookPresentInFavList

    private val _isBookPresentInReadingList = MutableSharedFlow<DoorCommand>()
    val isBookPresentInReadingList: SharedFlow<DoorCommand> = _isBookPresentInReadingList

    override val eventList: List<EventType>
        get() = listOf(
            EventType.SubscribeType("SubscribeBookById", this),
            EventType.SubscribeType("SubscribeReadingList", this),
            EventType.SubscribeType("SubscribeSearchResult", this),
            EventType.SubscribeType("SubscribeFavList", this),
            EventType.SubscribeType("SubscribeCheckFavBook", this),
            EventType.SubscribeType("SubscribeCheckReadingListBook", this),
        )

    override val messageList: List<MessageType>
        get() = listOf(
            MessageType.SendType("GetBookById", this),
            MessageType.SendType("GetReadingList", this),
            MessageType.SendType("GetFavList", this),
            MessageType.SendType("GetSearchResult", this),
            MessageType.SendType("GetIfBookIsFav", this),
            MessageType.SendType("GetIfBookIsInReadingList", this),
            MessageType.SendType("AddBookIntoFavList", this),
            MessageType.SendType("RemoveBookFromFavList", this),
            MessageType.SendType("AddBookIntoReadingList", this),
            MessageType.SendType("RemoveBookFromReadingList", this),

            MessageType.ReceiveType("ReceivedBookByIdResponse", this),
            MessageType.ReceiveType("ReceivedBookListResponse", this),
        )

    override fun init(context: Context) {

    }

    override fun onReceive(message: DoorCommand) {
        if (message.messageName == "ReceivedBookByIdResponse") {
            println("Shubham: Received book response: ${message}")
        }
    }

    override fun publish(message: DoorCommand): SharedFlow<DoorCommand> {
        TODO("Not yet implemented")
    }

    override fun subscribe(
        subscription: SharedFlow<DoorCommand>,
        featureCommand: DoorCommand,
    ) {
        collectSubscription(subscription, featureCommand)
    }

    private fun collectSubscription(
        flow: SharedFlow<DoorCommand>,
        command: DoorCommand,
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
            } else if (command.messageName == "SubscribeCheckFavBook") {
                flow.collect {
                    _isBookPresentInFavList.emit(it)
                }
            } else if (command.messageName == "SubscribeCheckReadingListBook") {
                flow.collect {
                    _isBookPresentInReadingList.emit(it)
                }
            } else {
                flow.collect {
                    println("Shubham: Subscription: command: $command, flow: ${it}")
                }
            }
        }
    }

    internal fun getSearchResult(query: String, startIndex: Int, count: Int) {
        messageSender.send(
            DoorCommand(
                messageName = "GetSearchResult",
                payload = JsonObject().apply {
                    addProperty("query", query)
                    addProperty("startIndex", startIndex)
                    addProperty("count", count)
                },
                doorName = this.name
            )
        )
    }

    internal fun getFavBookList() {
        messageSender.send(
            DoorCommand(
                messageName = "GetFavList",
                doorName = this.name
            )
        )
    }

    internal fun getReadingList() {
        messageSender.send(
            DoorCommand(
                messageName = "GetReadingList",
                doorName = this.name
            )
        )
    }

    internal fun getBookById(bookId: String) {
        val payload = JsonObject().apply {
            addProperty("id", bookId)
        }
        messageSender.send(
            DoorCommand(
                messageName = "GetBookById",
                payload = payload,
                doorName = this.name
            )
        )
    }

    internal fun checkIfBookIsFav(id: String) {
        val payload = JsonObject().apply {
            addProperty("id", id)
        }
        messageSender.send(
            DoorCommand(
                messageName = "GetIfBookIsFav",
                payload = payload,
                doorName = this.name
            )
        )
    }

    internal fun checkIfBookIsInReadingList(id: String) {
        val payload = JsonObject().apply {
            addProperty("id", id)
        }
        messageSender.send(
            DoorCommand(
                messageName = "GetIfBookIsInReadingList",
                payload = payload,
                doorName = this.name
            )
        )
    }

    internal fun addBookIntoFavList(book: Book) {
        val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
        messageSender.send(
            DoorCommand(
                messageName = "AddBookIntoFavList",
                payload = jsonObject,
                doorName = this.name
            )
        )
    }

    internal fun removeBookFromFavList(book: Book) {
        val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
        messageSender.send(
            DoorCommand(
                messageName = "RemoveBookFromFavList",
                payload = jsonObject,
                doorName = this.name
            )
        )
    }

    internal fun addBookIntoReadingList(book: Book) {
        val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
        messageSender.send(
            DoorCommand(
                messageName = "AddBookIntoReadingList",
                payload = jsonObject,
                doorName = this.name
            )
        )
    }

    internal fun removeBookFromReadingList(book: Book) {
        val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
        messageSender.send(
            DoorCommand(
                messageName = "RemoveBookFromReadingList",
                payload = jsonObject,
                doorName = this.name
            )
        )
    }
}
