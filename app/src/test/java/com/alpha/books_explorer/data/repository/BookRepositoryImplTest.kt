package com.alpha.books_explorer.data.repository

import com.alpha.books_explorer.data.local.dao.FavBookDao
import com.alpha.books_explorer.data.local.dao.ReadingListDao
import com.alpha.books_explorer.data.remote.BookApiService
import com.alpha.books_explorer.data.remote.dto.BookSearchResponse
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlinx.coroutines.test.TestScope

class BookRepositoryImplTest {

    private val api = mockk<BookApiService>()
    private val localDao = mockk<FavBookDao>()
    private val readingListDao = mockk<ReadingListDao>()

    @Test
    fun getBooks() = runTest {
        coEvery { api.searchBooks(any(), any(), any()) } returns
            BookSearchResponse(
                items = listOf(
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
                    )
                )
            )

        val repo = BookRepositoryImpl(api, localDao, readingListDao)
        val first = repo.getBooks("android").first()

        assertThat(first).containsExactly(
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
            )
        )
    }

    @Test
    fun getBooksById() = runTest {
        coEvery { api.getBookById(any()) } returns
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
            )


        val repo = BookRepositoryImpl(api, localDao, readingListDao)
        val first = repo.getBookById("x1")

        assertThat(first.first()).isEqualTo(
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
            )
        )
    }
}
