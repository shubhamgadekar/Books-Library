package com.alpha.books_explorer.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltViewModel
open class SearchViewModel
@Inject
constructor(
    private val mainAppDoor: MainAppDoor,
    private val getBooksUseCase: GetBooksUseCase,
) : ViewModel() {
    private val _searchText = MutableSharedFlow<String>()
    val searchText: SharedFlow<String> = _searchText

    private val _searchBookList = MutableStateFlow(SearchUiState())
    val searchBookList: StateFlow<SearchUiState> = _searchBookList

    init {
        resetScreen()
        viewModelScope.launch {
            mainAppDoor.searchList.collect {
                val type = object : TypeToken<List<Book>>() {}.type
                val books: List<Book> = Gson().fromJson(it.payload?.getAsJsonArray("books"), type)

                _searchBookList.value = SearchUiState(books = books)
            }
        }

        viewModelScope.launch {
            _searchText.debounce { 300 }.collect {
                if (it.isNotEmpty()) {
                    _searchBookList.value = SearchUiState(isLoading = true)
                    mainAppDoor.getSearchResult(it)
//                    val result = getBooksUseCase.invokePaging(it)
//                    _searchBookList.value = SearchUiState(books = result)
                }
            }
        }
    }

    fun resetScreen() {
        _searchBookList.value = SearchUiState()
    }

    fun updateSearchText(text: String) {
        viewModelScope.launch {
            _searchText.emit(text)
        }
    }
}
