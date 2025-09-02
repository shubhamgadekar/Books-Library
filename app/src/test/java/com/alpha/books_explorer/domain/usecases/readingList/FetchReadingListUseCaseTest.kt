package com.alpha.books_explorer.domain.usecases.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.readingList.FetchReadingListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class FetchReadingListUseCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var fetchReadingListUseCase: FetchReadingListUseCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        fetchReadingListUseCase = FetchReadingListUseCase(bookRepository)
    }

    @Test
    fun testInvoke() = runTest {
        val book = Book(
            id = "x1",
            volumeInfo = VolumeInfo(
                title = "Title",
                authors = listOf("John"),
                description = null,
                publisher = null,
                publishedDate = null,
                subtitle = null,
                imageLinks = null
            )
        )

        val list: List<Book> = listOf(book)
        coEvery { bookRepository.getReadingListBooks() } returns flowOf(list)

        fetchReadingListUseCase.invoke()
    }
}
