package com.alpha.books_explorer.presentation.ui.wishList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.usecase.FavList.FetchFavListUseCase
import com.alpha.books_explorer.presentation.ui.home.HomeUiState
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
class WishlistViewModel
@Inject
constructor(
    private val mainAppDoor: MainAppDoor,
    private val getFavBooksUseCase: FetchFavListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            mainAppDoor.favList
                .catch { e ->
                    _uiState.value = HomeUiState(error = e.message ?: "Unknown error")
                }
                .collect {
                    val type = object : TypeToken<List<Book>>() {}.type
                    val books: List<Book> = Gson().fromJson(it.payload?.getAsJsonArray("books"), type)
                    _uiState.value = HomeUiState(books = books)
                }
        }
        loadBooks()
        mainAppDoor.getFavBookList()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
//            delay(200)
//            getFavBooksUseCase.invoke()
//                .catch { e ->
//                    _uiState.value = HomeUiState(error = e.message ?: "Unknown error")
//                }
//                .collect { books ->
//                    _uiState.value = HomeUiState(books = books)
//                }
        }
    }
}
