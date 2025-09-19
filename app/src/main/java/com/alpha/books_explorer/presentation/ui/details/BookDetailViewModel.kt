package com.alpha.books_explorer.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.usecase.FavList.AddIntoFavListUseCase
import com.alpha.books_explorer.domain.usecase.FavList.IsBookPresentInFavListUserCase
import com.alpha.books_explorer.domain.usecase.FavList.RemoveFromFavListUseCase
import com.alpha.books_explorer.domain.usecase.GetBookByIdUserCase
import com.alpha.books_explorer.domain.usecase.readingList.AddIntoReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.IsBookPresentInReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.RemoveFromReadingListUseCase
import com.alpha.data.DataDoor
import com.alpha.myplatformdoor.MessageBus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class BookDetailViewModel
@Inject
constructor(
    private val appDoor: MainAppDoor,
    private val dataDoor: DataDoor,
    private val bus: MessageBus,
    private val getBookById: GetBookByIdUserCase,
    private val addIntoFavListUseCase: AddIntoFavListUseCase,
    private val isBookPresentInFavListUserCase: IsBookPresentInFavListUserCase,
    private val removeFromFavListUseCase: RemoveFromFavListUseCase,
    private val addIntoReadingListUseCase: AddIntoReadingListUseCase,
    private val isBookPresentInReadingListUseCase: IsBookPresentInReadingListUseCase,
    private val removeFromReadingListUseCase: RemoveFromReadingListUseCase,
) : ViewModel() {

//    @Inject
//    lateinit var bus: MessageBus

    private val _bookState = MutableStateFlow(BookDetailsUiState())
    val bookState: StateFlow<BookDetailsUiState> = _bookState

    private val _checkWishlistItem = MutableStateFlow(false)
    val checkWishlistItem: StateFlow<Boolean> = _checkWishlistItem

    private val _checkReadinglistItem = MutableStateFlow(false)
    val checkReadinglistItem: StateFlow<Boolean> = _checkReadinglistItem

    init {
//        viewModelScope.launch {
//            bus.messages.collect { msg ->
//                if (msg.messageName == Messages.GetStudentsData.name) {
//                    println("Shubham: bus.messages = GetStudentsData")
//                }
//            }
//        }
//
//        viewModelScope.launch {
//            dataDoor.handle(
//                FeatureCommand(
//                    messageName = "getBookById",
//                    payload = JsonObject().apply {
//                        addProperty("id", "1")
//                    }
//                )
//            ).collect { bookRes ->
//                bookRes.let {
//                    when (it) {
//                        is FeatureResult.Failure -> {
//                            println("Shubham: Failed = ${it.throwable.message}")
//                        }
//
//                        is FeatureResult.Success -> {
//                            println("Shubham: Success = ${it.data}")
//                        }
//                    }
//                }
//            }
//        }
    }

    init {
        viewModelScope.launch {
            appDoor.bookById
                .catch {
                    _bookState.value = BookDetailsUiState(error = it.message)
                }
                .collect {
                    val type = object : TypeToken<Book>() {}.type
                    val book: Book = Gson().fromJson(it.payload, type)

                    _bookState.value = BookDetailsUiState(book = book)
                    checkWishlistItem(book)
                    checkReadinglistItem(book)
                }
        }
    }

    fun checkWishlistItem(book: Book?) {
        if (book == null) {
            _checkWishlistItem.value = false
            return
        }
        viewModelScope.launch {
            isBookPresentInFavListUserCase.invoke(book).collect {
                _checkWishlistItem.value = it
            }
        }
    }

    fun checkReadinglistItem(book: Book?) {
        if (book == null) {
            _checkReadinglistItem.value = false
            return
        }
        viewModelScope.launch {
            isBookPresentInReadingListUseCase.invoke(book).collect {
                _checkReadinglistItem.value = it
            }
        }
    }

    fun fetchBookById(bookId: String) {
        viewModelScope.launch {
            appDoor.getBookById(bookId)
            _bookState.value = BookDetailsUiState(isLoading = true)
//            delay(200)
//            getBookById.invoke(bookId)
//                .catch {
//                    _bookState.value = BookDetailsUiState(error = it.message)
//                }
//                .collect {
//                    _bookState.value = BookDetailsUiState(book = it)
//                    checkWishlistItem(it)
//                    checkReadinglistItem(it)
//                }
        }
    }

    fun addToWishlist(book: Book) {
        viewModelScope.launch {
            addIntoFavListUseCase.invoke(book)
            delay(200)
            checkWishlistItem(book)
        }
    }

    fun removeFromWishList(book: Book) {
        viewModelScope.launch {
            removeFromFavListUseCase.invoke(book)
            delay(200)
            checkWishlistItem(book)
        }
    }

    fun addToReadinglist(book: Book) {
        viewModelScope.launch {
            addIntoReadingListUseCase.invoke(book)
            delay(200)
            checkReadinglistItem(book)
        }
    }

    fun removeFromReadingList(book: Book) {
        viewModelScope.launch {
            removeFromReadingListUseCase.invoke(book)
            delay(200)
            checkReadinglistItem(book)
        }
    }
}
