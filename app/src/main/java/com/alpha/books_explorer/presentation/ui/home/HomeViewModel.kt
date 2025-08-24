package com.alpha.books_explorer.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import com.alpha.books_explorer.domain.usecase.FavList.FetchFavListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.FetchReadingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchReadingListUseCase: FetchReadingListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
//        loadBooks("android")
    }

    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            fetchReadingListUseCase.invoke()
                .catch { e ->
                    _uiState.value = HomeUiState(error = e.message ?: "Unknown error")
                }
                .collect { books ->
                    _uiState.value = HomeUiState(books = books)
                }
        }
    }
}
