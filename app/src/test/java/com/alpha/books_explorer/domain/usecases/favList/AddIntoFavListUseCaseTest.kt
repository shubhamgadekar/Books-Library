package com.alpha.books_explorer.domain.usecases.favList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.FavList.AddIntoFavListUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.runTest

class AddIntoFavListUseCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var addIntoFavListUseCase: AddIntoFavListUseCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        addIntoFavListUseCase = AddIntoFavListUseCase(bookRepository)
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

        coJustRun { bookRepository.addIntoFavListBooks(book) }

        addIntoFavListUseCase.invoke(book)
    }
}
