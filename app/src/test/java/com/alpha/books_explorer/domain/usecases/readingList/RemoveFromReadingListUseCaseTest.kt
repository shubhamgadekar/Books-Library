package com.alpha.books_explorer.domain.usecases.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.readingList.IsBookPresentInReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.RemoveFromReadingListUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class RemoveFromReadingListUseCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var removeFromReadingListUseCase: RemoveFromReadingListUseCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        removeFromReadingListUseCase = RemoveFromReadingListUseCase(bookRepository)
    }

    @Test
    fun testInvoke() = runTest {
        val book = Book(
            id = "x1", volumeInfo = VolumeInfo(
                title = "Title",
                authors = listOf("John"),
                description = null,
                publisher = null,
                publishedDate = null,
                subtitle = null,
                imageLinks = null
            )
        )

        coJustRun { bookRepository.deleteFromReadingListBooks(book) }
        removeFromReadingListUseCase.invoke(book)
    }
}
