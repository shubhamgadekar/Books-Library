package com.alpha.books_explorer.domain.usecase.FavList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject

class AddIntoFavListUseCase
    @Inject
    constructor(
        private val bookRepository: BookRepository,
    ) {
        suspend fun invoke(book: Book) {
            bookRepository.addIntoFavListBooks(book)
        }
    }
