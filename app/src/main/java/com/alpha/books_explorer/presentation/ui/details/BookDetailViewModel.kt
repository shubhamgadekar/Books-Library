package com.alpha.books_explorer.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
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
internal class BookDetailViewModel
@Inject
constructor(
    private val appDoor: MainAppDoor,
) : ViewModel() {

    private val _bookState = MutableStateFlow(BookDetailsUiState())
    val bookState: StateFlow<BookDetailsUiState> = _bookState

    private val _checkWishlistItem = MutableStateFlow(false)
    val checkWishlistItem: StateFlow<Boolean> = _checkWishlistItem

    private val _checkReadinglistItem = MutableStateFlow(false)
    val checkReadinglistItem: StateFlow<Boolean> = _checkReadinglistItem

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

        viewModelScope.launch {
            appDoor.isBookPresentInFavList
                .catch {

                }
                .collect {
                    val isfav = it.payload?.get("isFav")?.asBoolean == true
                    _checkWishlistItem.value = isfav
                }
        }

        viewModelScope.launch {
            appDoor.isBookPresentInReadingList
                .catch {

                }
                .collect {
                    val isfav = it.payload?.get("isInReadingList")?.asBoolean == true
                    _checkReadinglistItem.value = isfav
                }
        }
    }

    internal fun checkWishlistItem(book: Book?) {
        if (book == null) {
            _checkWishlistItem.value = false
            return
        }
        appDoor.checkIfBookIsFav(book.id)
    }

    internal fun checkReadinglistItem(book: Book?) {
        if (book == null) {
            _checkReadinglistItem.value = false
            return
        }
        appDoor.checkIfBookIsInReadingList(book.id)
    }

    internal fun fetchBookById(bookId: String) {
        viewModelScope.launch {
            appDoor.getBookById(bookId)
            _bookState.value = BookDetailsUiState(isLoading = true)
        }
    }

    internal fun addToWishlist(book: Book) {
        viewModelScope.launch {
            appDoor.addBookIntoFavList(book)
            delay(200)
            appDoor.getFavBookList()
            checkWishlistItem(book)
        }
    }

    internal fun removeFromWishList(book: Book) {
        viewModelScope.launch {
            appDoor.removeBookFromFavList(book)
            delay(200)
            appDoor.getFavBookList()
            checkWishlistItem(book)
        }
    }

    internal fun addToReadinglist(book: Book) {
        viewModelScope.launch {
            appDoor.addBookIntoReadingList(book)
            delay(200)
            appDoor.getReadingList()
            checkReadinglistItem(book)
        }
    }

    internal fun removeFromReadingList(book: Book) {
        viewModelScope.launch {
            appDoor.removeBookFromReadingList(book)
            delay(200)
            appDoor.getReadingList()
            checkReadinglistItem(book)
        }
    }
}
