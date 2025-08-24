package com.alpha.books_explorer.presentation.ui.search

import androidx.paging.PagingData
import com.alpha.books_explorer.domain.model.Book
import kotlinx.coroutines.flow.Flow

data class SearchScreenUiState(
    val isLoading: Boolean = false,
    val books: Flow<PagingData<Book>>? = null,
    val error: String? = null
)
