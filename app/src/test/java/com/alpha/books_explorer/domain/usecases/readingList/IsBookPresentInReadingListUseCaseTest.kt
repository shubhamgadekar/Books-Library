package com.alpha.books_explorer.domain.usecases.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.readingList.FetchReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.IsBookPresentInReadingListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class IsBookPresentInReadingListUseCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var isBookPresentInReadingListUseCase: IsBookPresentInReadingListUseCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        isBookPresentInReadingListUseCase = IsBookPresentInReadingListUseCase(bookRepository)
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

        coEvery { bookRepository.isBookPresentInReadingList(book) } returns flowOf(false)
        isBookPresentInReadingListUseCase.invoke(book)
    }
}
