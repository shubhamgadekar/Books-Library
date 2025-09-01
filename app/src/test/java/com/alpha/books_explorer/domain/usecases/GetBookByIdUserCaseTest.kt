package com.alpha.books_explorer.domain.usecases

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.GetBookByIdUserCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf

class GetBookByIdUserCaseTest {

    private lateinit var getBookByIdUserCase: GetBookByIdUserCase

    private lateinit var bookRepository: BookRepository

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        getBookByIdUserCase = GetBookByIdUserCase(bookRepository)
    }

    @Test
    fun testInvoke() {
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
        coEvery { bookRepository.getBookById("x1") } returns flowOf(book)

        getBookByIdUserCase.invoke("x1")
    }
}
