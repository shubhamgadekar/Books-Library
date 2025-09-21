package com.alpha.data.remote.dto

import com.alpha.data.Book

internal data class BookSearchResponse(
    val items: List<Book>?,
)
