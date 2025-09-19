package com.alpha.books_explorer.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.usecase.readingList.FetchReadingListUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val appDoor: MainAppDoor,
    private val fetchReadingListUseCase: FetchReadingListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadBooks()
        appDoor.getReadingList()
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            appDoor.readingList.collect {
                val type = object : TypeToken<List<Book>>() {}.type
                val books: List<Book> = Gson().fromJson(it.payload?.getAsJsonArray("books"), type)
                _uiState.value = HomeUiState(books = books)
            }
//            fetchReadingListUseCase.invoke()
//                .catch { e ->
//                    _uiState.value = HomeUiState(error = e.message ?: "Unknown error")
//                }
//                .distinctUntilChanged()
//                .collect { books ->
//                    _uiState.value = HomeUiState(books = books)
//                }
        }
    }
}
