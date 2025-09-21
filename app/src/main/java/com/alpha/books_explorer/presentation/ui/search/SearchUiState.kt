package com.alpha.books_explorer.presentation.ui.search

import com.alpha.books_explorer.domain.model.Book

internal data class SearchUiState(
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null,
    val querySearched: Boolean = false
)
