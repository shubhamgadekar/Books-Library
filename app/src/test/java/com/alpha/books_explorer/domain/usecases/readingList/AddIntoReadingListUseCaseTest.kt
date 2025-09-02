package com.alpha.books_explorer.domain.usecases.readingList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.readingList.AddIntoReadingListUseCase
import io.mockk.coJustRun
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class AddIntoReadingListUseCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var addIntoReadingListUseCase: AddIntoReadingListUseCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        addIntoReadingListUseCase = AddIntoReadingListUseCase(bookRepository)
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

        coJustRun { bookRepository.addIntoReadingListBooks(book) }

        addIntoReadingListUseCase.invoke(book)
    }
}
