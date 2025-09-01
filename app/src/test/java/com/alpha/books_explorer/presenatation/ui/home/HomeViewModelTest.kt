package com.alpha.books_explorer.presenatation.ui.home

import app.cash.turbine.test
import com.alpha.books_explorer.MainDispatcherRule
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.usecase.readingList.FetchReadingListUseCase
import com.alpha.books_explorer.presentation.ui.home.HomeUiState
import com.alpha.books_explorer.presentation.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fetchReadingListUseCase: FetchReadingListUseCase

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        fetchReadingListUseCase = mockk<FetchReadingListUseCase>()
        homeViewModel = HomeViewModel(fetchReadingListUseCase)
    }

    @Test
    fun `test home view model`() = runTest {
        val books: List<Book> = listOf()

        coEvery { fetchReadingListUseCase.invoke() } returns flowOf(books)

        homeViewModel.uiState.test {
            assertEquals(HomeUiState(isLoading = false), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        homeViewModel.loadBooks()

        homeViewModel.uiState.test {
            assertEquals(HomeUiState(isLoading = true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test home view model valid output`() = runTest {
        val book1 = Book(
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

        val book2 = Book(
            id = "x2", volumeInfo = VolumeInfo(
                title = "Title",
                authors = listOf("John"),
                description = null,
                publisher = null,
                publishedDate = null,
                subtitle = null,
                imageLinks = null
            )
        )

        val books: List<Book> = listOf(book1, book2)

        coEvery { fetchReadingListUseCase.invoke() } returns flowOf(books)

        homeViewModel.loadBooks()

        homeViewModel.uiState.test {
            assertEquals(HomeUiState(isLoading = true), awaitItem())
            assertEquals(HomeUiState(isLoading = false, books = books), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
