package com.alpha.books_explorer.presenatation.ui.wishlist

import app.cash.turbine.test
import com.alpha.books_explorer.MainDispatcherRule
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.usecase.FavList.FetchFavListUseCase
import com.alpha.books_explorer.presentation.ui.home.HomeUiState
import com.alpha.books_explorer.presentation.ui.wishList.WishlistViewModel
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class WishlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var wishlistViewModel: WishlistViewModel

    @MockK
    private lateinit var getFavBooksUseCase: FetchFavListUseCase

    @Before
    fun setup() {
        getFavBooksUseCase = mockk<FetchFavListUseCase>()
        wishlistViewModel = WishlistViewModel(getFavBooksUseCase)
    }

    @Test
    fun testLoadBooks() = runTest {
        coEvery { getFavBooksUseCase.invoke() } returns flowOf(listOf<Book>())

        wishlistViewModel.loadBooks()

        wishlistViewModel.uiState.test {
            assertEquals(HomeUiState(isLoading = true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testLoadBooksWithData() = runTest {
        val book1 = Book(
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

        val book2 = Book(
            id = "x2",
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

        coEvery { getFavBooksUseCase.invoke() } returns flowOf(listOf(book1, book2))

        wishlistViewModel.loadBooks()

        wishlistViewModel.uiState.test {
            assertEquals(HomeUiState(isLoading = true), awaitItem())
            assertEquals(HomeUiState(isLoading = false, books = listOf(book1, book2)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
