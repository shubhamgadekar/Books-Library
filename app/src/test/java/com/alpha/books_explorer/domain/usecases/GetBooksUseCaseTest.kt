package com.alpha.books_explorer.domain.usecases

import androidx.paging.PagingData
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetBooksUseCaseTest {

    lateinit var getBooksUseCase: GetBooksUseCase

    lateinit var repository: BookRepository

    @Before
    fun setup() {
        repository = mockk<BookRepository>()
        getBooksUseCase = GetBooksUseCase(repository)
    }

    @Test
    fun testInvoke() {
        val books: List<Book> = listOf()
        coEvery { repository.getBooks("id") } returns flowOf(books)
        getBooksUseCase.invoke("id")
    }
}
