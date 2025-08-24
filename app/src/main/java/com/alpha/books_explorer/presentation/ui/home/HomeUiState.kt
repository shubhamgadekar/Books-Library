package com.alpha.books_explorer.presentation.ui.home

import com.alpha.books_explorer.domain.model.Book

data class HomeUiState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)