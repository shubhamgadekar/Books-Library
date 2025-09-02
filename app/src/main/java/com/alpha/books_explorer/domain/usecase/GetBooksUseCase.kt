package com.alpha.books_explorer.domain.usecase

import androidx.paging.PagingData
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetBooksUseCase
@Inject
constructor(
    private val repository: BookRepository,
) {
    fun invoke(query: String): Flow<List<Book>> {
        return repository.getBooks(query)
    }

    fun invokePaging(query: String): Flow<PagingData<Book>> {
        return repository.getBooksFromPaging(query)
    }
}
