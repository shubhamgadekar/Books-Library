package com.alpha.books_explorer.domain.usecase.FavList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class IsBookPresentInFavListUserCase
    @Inject
    constructor(
        private val localDbRepository: BookRepository,
    ) {
        fun invoke(book: Book): Flow<Boolean> {
            return localDbRepository.isBookPresentInFavList(book)
        }
    }
