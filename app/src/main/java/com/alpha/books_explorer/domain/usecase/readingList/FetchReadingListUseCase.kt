package com.alpha.books_explorer.domain.usecase.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FetchReadingListUseCase
@Inject
constructor(
    private val repository: BookRepository,
) {
    fun invoke(): Flow<List<Book>> {
        return repository.getReadingListBooks()
    }
}
