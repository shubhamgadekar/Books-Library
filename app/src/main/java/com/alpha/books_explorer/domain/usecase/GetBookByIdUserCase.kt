package com.alpha.books_explorer.domain.usecase

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBookByIdUserCase
@Inject
constructor(
    private val repository: BookRepository,
) {
    fun invoke(id: String): Flow<Book> {
        return repository.getBookById(id)
    }
}
