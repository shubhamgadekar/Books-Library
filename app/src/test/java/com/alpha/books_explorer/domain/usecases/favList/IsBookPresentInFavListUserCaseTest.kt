package com.alpha.books_explorer.domain.usecases.favList

import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.FavList.FetchFavListUseCase
import com.alpha.books_explorer.domain.usecase.FavList.IsBookPresentInFavListUserCase
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class IsBookPresentInFavListUserCaseTest {

    private lateinit var bookRepository: BookRepository

    private lateinit var isBookPresentInFavListUserCase: IsBookPresentInFavListUserCase

    @Before
    fun setup() {
        bookRepository = mockk<BookRepository>()
        isBookPresentInFavListUserCase = IsBookPresentInFavListUserCase(bookRepository)
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

        coEvery { bookRepository.isBookPresentInFavList(book) } returns flowOf(false)

        isBookPresentInFavListUserCase.invoke(book)
    }
}
