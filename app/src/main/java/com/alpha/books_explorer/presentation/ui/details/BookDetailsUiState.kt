package com.alpha.books_explorer.presentation.ui.details

import com.alpha.books_explorer.domain.model.Book

internal data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null,
)
