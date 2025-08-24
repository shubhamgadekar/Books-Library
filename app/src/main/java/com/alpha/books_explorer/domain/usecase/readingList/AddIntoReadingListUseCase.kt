package com.alpha.books_explorer.domain.usecase.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject

class AddIntoReadingListUseCase
    @Inject
    constructor(
        private val repository: BookRepository,
    ) {
        suspend fun invoke(book: Book) {
            repository.addIntoReadingListBooks(book)
        }
    }
