package com.alpha.books_explorer.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltViewModel
open class SearchScreenViewModel
    @Inject
    constructor(
        private val getBooksUseCase: GetBooksUseCase,
    ) : ViewModel() {
        private val _searchText = MutableSharedFlow<String>()
        val searchText: SharedFlow<String> = _searchText

        private val _searchBookList = MutableStateFlow(SearchScreenUiState())
        val searchBookList: StateFlow<SearchScreenUiState> = _searchBookList

        init {
            resetScreen()
            viewModelScope.launch {
                _searchText.debounce { 300 }.collect {
                    if (it.isNotEmpty()) {
                        _searchBookList.value = SearchScreenUiState(isLoading = true)
                        val result = getBooksUseCase.invokePaging(it)
                        _searchBookList.value = SearchScreenUiState(books = result)
                    }
                }
            }
        }

        fun resetScreen() {
            _searchBookList.value = SearchScreenUiState()
        }

        fun updateSearchText(text: String) {
            viewModelScope.launch {
                _searchText.emit(text)
            }
        }
    }
