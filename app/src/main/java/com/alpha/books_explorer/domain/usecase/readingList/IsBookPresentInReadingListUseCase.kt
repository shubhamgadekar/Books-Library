package com.alpha.books_explorer.domain.usecase.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsBookPresentInReadingListUseCase @Inject constructor(
    private val repository: BookRepository
) {
    fun invoke(book: Book): Flow<Boolean> {
        return repository.isBookPresentInReadingList(book)
    }
}
