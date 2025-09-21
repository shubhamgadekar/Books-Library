package com.alpha.books_explorer.presentation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.books_explorer.MainAppDoor
import com.alpha.books_explorer.domain.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltViewModel
internal  class SearchViewModel
@Inject
constructor(
    private val mainAppDoor: MainAppDoor
) : ViewModel() {
    private val _searchText = MutableStateFlow<String>("")
    val searchText: StateFlow<String> = _searchText

    private val _searchBookList = MutableStateFlow(SearchUiState())
    val searchBookList: StateFlow<SearchUiState> = _searchBookList

    private var currentPage = 1
    private var isLoadingNextPage = false
    private var canLoadMorePages = true
    private var isPaginating = false

    init {
        viewModelScope.launch {
            mainAppDoor.searchList.collect {
                try {
                    val type = object : TypeToken<List<Book>>() {}.type
                    val books: List<Book> = Gson().fromJson(it.payload?.getAsJsonArray("books"), type)

                    val currentBooks = if (isPaginating) {
                        _searchBookList.value.books
                    } else {
                        isPaginating = true
                        emptyList()
                    }
                    val newBooks = currentBooks + books

                    canLoadMorePages = true

                    _searchBookList.value = _searchBookList.value.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        books = newBooks,
                        error = null,
                        querySearched = true
                    )
                } catch (e: Exception) {
                    _searchBookList.value = _searchBookList.value.copy(
                        isLoading = false,
                        isLoadingNextPage = false,
                        error = e.message ?: "An unknown error occurred",
                        querySearched = true
                    )
                } finally {
                    isLoadingNextPage = false
                }
            }
        }

        viewModelScope.launch {
            _searchText
                .debounce(500L)
                .collectLatest { query ->
                    if (query.isNotBlank()) {
                        resetAndSearchBooks(query)
                    } else {
                        _searchBookList.value = SearchUiState(books = emptyList(), querySearched = false)
                        currentPage = 1
                        canLoadMorePages = true
                    }
                }
        }
    }

    private fun fetchBooks(query: String, page: Int, isPaginating: Boolean = false) {
        isLoadingNextPage = isPaginating
        if (!isPaginating) {
            _searchBookList.value = _searchBookList.value.copy(isLoading = true, error = null)
        } else {
            _searchBookList.value = _searchBookList.value.copy(isLoadingNextPage = true, error = null)
        }
        mainAppDoor.getSearchResult(query, (page - 1) * 20, 20)
    }

    internal fun loadNextPage() {
        if (isLoadingNextPage || !canLoadMorePages || _searchText.value.isBlank()) {
            return
        }
        currentPage++
        fetchBooks(_searchText.value, currentPage, isPaginating = true)
    }

    private fun resetAndSearchBooks(query: String) {
        currentPage = 1
        canLoadMorePages = true
        _searchBookList.value = SearchUiState(isLoading = true, books = emptyList(), querySearched = true)
        fetchBooks(query, currentPage)
    }

    internal fun updateSearchText(text: String) {
        viewModelScope.launch {
            _searchText.emit(text)
        }
    }
}
