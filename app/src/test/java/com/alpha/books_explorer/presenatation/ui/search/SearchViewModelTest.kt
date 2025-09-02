package com.alpha.books_explorer.presenatation.ui.search

import androidx.paging.PagingData
import com.alpha.books_explorer.MainDispatcherRule
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import com.alpha.books_explorer.presentation.ui.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    lateinit var getBooksUseCase: GetBooksUseCase

    @Before
    fun before() {
        val repo = mockk<BookRepository>()
        getBooksUseCase = GetBooksUseCase(repo)
    }

    @Test
    fun `searchBooks exposes paging data`() = runTest {
        val fakeFlow = flowOf(
            PagingData.from(
                listOf(
                    Book(
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
                    ),
                    Book(
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
                )
            )
        )

        coEvery { getBooksUseCase.invokePaging("query") } returns fakeFlow

        val vm = SearchViewModel(
            getBooksUseCase = getBooksUseCase
        )

        vm.updateSearchText("query")

        delay(500)

        assert(vm.searchBookList.value.books?.count() == (fakeFlow.count()))
    }

    @Test
    fun `searchBooks exposes paging data2`() = runTest {
        val fakeFlow = flowOf(
            PagingData.from(
                listOf<Book>()
            )
        )

        coEvery { getBooksUseCase.invokePaging("") } returns fakeFlow

        val vm = SearchViewModel(
            getBooksUseCase = getBooksUseCase
        )

        vm.updateSearchText("")

        delay(500)

        assert(vm.searchBookList.value.books == null)
    }
}
