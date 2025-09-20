package com.alpha.data

import android.content.Context
import com.alpha.data.repository.BookRepositoryImpl
import com.alpha.myplatformdoor.messageTypes.EventType
import com.alpha.myplatformdoor.FeatureCommand
import com.alpha.myplatformdoor.FeatureEntry
import com.alpha.myplatformdoor.messageTypes.MessageType
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
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

    private val _bookByIdFlow = MutableSharedFlow<FeatureCommand>()
    private val bookByIdFlow: SharedFlow<FeatureCommand> = _bookByIdFlow

    private val _readingList = MutableSharedFlow<FeatureCommand>()
    private val readingList: SharedFlow<FeatureCommand> = _readingList

    private val _searchBooks = MutableSharedFlow<FeatureCommand>()
    private val searchBooks: SharedFlow<FeatureCommand> = _searchBooks

    private val _favBooks = MutableSharedFlow<FeatureCommand>()
    private val favBooks: SharedFlow<FeatureCommand> = _favBooks

    private val _isBookPresentInFavList = MutableSharedFlow<FeatureCommand>()
    val isBookPresentInFavList: SharedFlow<FeatureCommand> = _isBookPresentInFavList

    private val _isBookPresentInReadingList = MutableSharedFlow<FeatureCommand>()
    val isBookPresentInReadingList: SharedFlow<FeatureCommand> = _isBookPresentInReadingList

    override fun handle(command: FeatureCommand): Flow<FeatureCommand> = flow {
        val response = JsonObject().apply {
            addProperty("status", "Domain handled")
        }

        bus.publish(FeatureCommand(messageName = "GetStudentsData", response))

        emit(
            FeatureCommand(
                messageName = command.messageName,
                payload = response
            )

        )
    }

    override fun init(context: Context) {

    }

    override val eventList: List<EventType>
        get() = listOf(
            EventType.PublishType("SubscribeBookById", this),
            EventType.PublishType("SubscribeReadingList", this),
            EventType.PublishType("SubscribeSearchResult", this),
            EventType.PublishType("SubscribeFavList", this),
            EventType.PublishType("SubscribeCheckFavBook", this),
            EventType.PublishType("SubscribeCheckReadingListBook", this),
        )


    override val messageList: List<MessageType>
        get() = listOf(
            MessageType.ReceiveType("GetBookById", this),
            MessageType.ReceiveType("GetReadingList", this),
            MessageType.ReceiveType("GetFavList", this),
            MessageType.ReceiveType("GetSearchResult", this),
            MessageType.ReceiveType("GetIfBookIsFav", this),
            MessageType.ReceiveType("GetIfBookIsInReadingList", this),
            MessageType.ReceiveType("AddBookIntoFavList", this),
            MessageType.ReceiveType("RemoveBookFromFavList", this),
            MessageType.ReceiveType("AddBookIntoReadingList", this),
            MessageType.ReceiveType("RemoveBookFromReadingList", this),

            MessageType.SendType("ReceivedBookByIdResponse", this),
            MessageType.SendType("ReceivedBookListResponse", this),
        )

    override fun onReceive(message: FeatureCommand) {
        if (message.messageName == "GetBookById") {
            CoroutineScope(Dispatchers.IO).launch {
                val book = bookRepositoryImpl.getBookById(message.payload?.get("id")?.asString ?: "")
                val jsonObject: JsonObject = Gson().toJsonTree(book).asJsonObject
                messageSender.send(
                    FeatureCommand(
                        messageName = "ReceivedBookByIdResponse",
                        payload = jsonObject,
                        doorName = this@DataDoor.name
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
                val books = bookRepositoryImpl.getBooks(
                    query = message.payload?.get("query")?.asString ?: "",
                    start = message.payload?.get("startIndex")?.asInt ?: 0,
                    count = message.payload?.get("count")?.asInt ?: 20,
                )
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
        } else if (message.messageName == "GetIfBookIsFav") {
            CoroutineScope(Dispatchers.IO).launch {
                val isFavBook = bookRepositoryImpl.isBookPresentInFavList(
                    id = message.payload?.get("id")?.asString ?: ""
                )

                val message = FeatureCommand(
                    messageName = "SubscribeCheckFavBook",
                    payload = JsonObject().apply {
                        addProperty("isFav", isFavBook)
                    }
                )
                _isBookPresentInFavList.emit(
                    message
                )
            }
        } else if (message.messageName == "GetIfBookIsInReadingList") {
            CoroutineScope(Dispatchers.IO).launch {
                val isFavBook = bookRepositoryImpl.isBookPresentInReadingList(
                    id = message.payload?.get("id")?.asString ?: ""
                )

                val message = FeatureCommand(
                    messageName = "SubscribeCheckReadingListBook",
                    payload = JsonObject().apply {
                        addProperty("isInReadingList", isFavBook)
                    }
                )
                _isBookPresentInReadingList.emit(
                    message
                )
            }
        } else if (message.messageName == "AddBookIntoFavList") {
            CoroutineScope(Dispatchers.IO).launch {
                val type = object : TypeToken<Book>() {}.type
                val book: Book = Gson().fromJson(message.payload, type)
                bookRepositoryImpl.addIntoFavListBooks(book)
            }
        } else if (message.messageName == "RemoveBookFromFavList") {
            CoroutineScope(Dispatchers.IO).launch {
                val type = object : TypeToken<Book>() {}.type
                val book: Book = Gson().fromJson(message.payload, type)
                bookRepositoryImpl.deleteFromFavListBooks(book)
            }
        } else if (message.messageName == "AddBookIntoReadingList") {
            CoroutineScope(Dispatchers.IO).launch {
                val type = object : TypeToken<Book>() {}.type
                val book: Book = Gson().fromJson(message.payload, type)
                bookRepositoryImpl.addIntoReadingListBooks(book)
            }
        } else if (message.messageName == "RemoveBookFromReadingList") {
            CoroutineScope(Dispatchers.IO).launch {
                val type = object : TypeToken<Book>() {}.type
                val book: Book = Gson().fromJson(message.payload, type)
                bookRepositoryImpl.deleteFromReadingListBooks(book)
            }
        }
    }

    override fun publish(message: FeatureCommand): SharedFlow<FeatureCommand> {
        return if (message.messageName == "SubscribeBookById") return bookByIdFlow
        else if (message.messageName == "SubscribeReadingList") return readingList
        else if (message.messageName == "SubscribeSearchResult") return searchBooks
        else if (message.messageName == "SubscribeFavList") return favBooks
        else if (message.messageName == "SubscribeCheckFavBook") return isBookPresentInFavList
        else if (message.messageName == "SubscribeCheckReadingListBook") return isBookPresentInReadingList
        else throw Exception("Message Not Subscribed")
    }

    override fun subscribe(
        subscription: SharedFlow<FeatureCommand>,
        featureCommand: FeatureCommand,
    ) {

    }
}
