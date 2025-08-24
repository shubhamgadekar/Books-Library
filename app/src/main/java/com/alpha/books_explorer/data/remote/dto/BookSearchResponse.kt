package com.alpha.books_explorer.data.remote.dto

import com.alpha.books_explorer.domain.model.Book

data class BookSearchResponse(
    val items: List<Book>?
)
