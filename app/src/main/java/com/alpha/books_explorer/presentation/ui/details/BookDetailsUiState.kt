package com.alpha.books_explorer.presentation.ui.details

import com.alpha.books_explorer.domain.model.Book

// sealed class BookDetailsUiState(data: Any) {
//    data class Error(val error: String): BookDetailsUiState(error)
//    data class BookToShow(val book: Book): BookDetailsUiState(book)
//    data class Loading(): BookDetailsUiState()
// }

data class BookDetailsUiState(
    val isLoading: Boolean = false,
    val book: Book? = null,
    val error: String? = null,
)
