package com.alpha.books_explorer.presenatation.ui.details

import app.cash.turbine.test
import com.alpha.books_explorer.MainDispatcherRule
import com.alpha.books_explorer.data.repository.BookRepositoryImpl
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.model.VolumeInfo
import com.alpha.books_explorer.domain.repository.BookRepository
import com.alpha.books_explorer.domain.usecase.FavList.AddIntoFavListUseCase
import com.alpha.books_explorer.domain.usecase.FavList.IsBookPresentInFavListUserCase
import com.alpha.books_explorer.domain.usecase.FavList.RemoveFromFavListUseCase
import com.alpha.books_explorer.domain.usecase.GetBookByIdUserCase
import com.alpha.books_explorer.domain.usecase.GetBooksUseCase
import com.alpha.books_explorer.domain.usecase.readingList.AddIntoReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.IsBookPresentInReadingListUseCase
import com.alpha.books_explorer.domain.usecase.readingList.RemoveFromReadingListUseCase
import com.alpha.books_explorer.presentation.ui.details.BookDetailViewModel
import com.alpha.books_explorer.presentation.ui.details.BookDetailsUiState
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.just
import io.mockk.justRun
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.kotlin.whenever
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

class BookDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var getBookById: GetBookByIdUserCase

    @Mock
    lateinit var addIntoFavListUseCase: AddIntoFavListUseCase

    @Mock
    lateinit var isBookPresentInFavListUserCase: IsBookPresentInFavListUserCase

    @Mock
    lateinit var removeFromFavListUseCase: RemoveFromFavListUseCase

    @Mock
    lateinit var addIntoReadingListUseCase: AddIntoReadingListUseCase

    @Mock
    lateinit var isBookPresentInReadingListUseCase: IsBookPresentInReadingListUseCase

    @Mock
    lateinit var removeFromReadingListUseCase: RemoveFromReadingListUseCase

    lateinit var bookDetailViewModel: BookDetailViewModel

    @Mock
    lateinit var repo: BookRepository

    @Before
    fun before() {
        repo = mockk<BookRepository>()
        getBookById = mockk<GetBookByIdUserCase>()
        addIntoFavListUseCase = mockk<AddIntoFavListUseCase>()
        isBookPresentInFavListUserCase = mockk<IsBookPresentInFavListUserCase>()
        removeFromFavListUseCase = mockk<RemoveFromFavListUseCase>()
        addIntoReadingListUseCase = mockk<AddIntoReadingListUseCase>()
        isBookPresentInReadingListUseCase = mockk<IsBookPresentInReadingListUseCase>()
        removeFromReadingListUseCase = mockk<RemoveFromReadingListUseCase>()

        bookDetailViewModel = BookDetailViewModel(
            getBookById,
            addIntoFavListUseCase,
            isBookPresentInFavListUserCase,
            removeFromFavListUseCase,
            addIntoReadingListUseCase,
            isBookPresentInReadingListUseCase,
            removeFromReadingListUseCase,
        )
    }

    @Test
    fun `check null with checkWishlistItem`() {
        bookDetailViewModel.checkWishlistItem(null)
        assertFalse(bookDetailViewModel.checkWishlistItem.value)
    }

    @Test
    fun `check non-null with checkWishlistItem`() {
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
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flow { false }

        bookDetailViewModel.checkWishlistItem(book)

        assertFalse(bookDetailViewModel.checkWishlistItem.value)
    }

    @Test
    fun `check null with checkReadinglistItem`() {

//        coEvery { isBookPresentInReadingListUseCase.invoke(null) } returns flow { false }

        bookDetailViewModel.checkReadinglistItem(null)

        assertFalse(bookDetailViewModel.checkReadinglistItem.value)
    }

    @Test
    fun `check non-null with checkReadinglistItem`() {

        val book = Book(
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

        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flow { false }

        bookDetailViewModel.checkReadinglistItem(book)

        assertFalse(bookDetailViewModel.checkWishlistItem.value)
    }

//    @Test
//    fun `check fetchBookById`() = runTest {
//
//        val book = Book(
//            id = "x2",
//            volumeInfo = VolumeInfo(
//                title = "Title",
//                authors = listOf("John"),
//                description = null,
//                publisher = null,
//                publishedDate = null,
//                subtitle = null,
//                imageLinks = null
//            )
//        )
//
//        coEvery { getBookById.invoke("x2") } returns flowOf(book)
////        whenever(getBookById.invoke("x2")).thenReturn(flowOf(book))
//
////        assert(bookDetailViewModel.bookState.value.book == null)
//
//        bookDetailViewModel.fetchBookById("x2")
//
//        bookDetailViewModel.bookState.test {
//            assertEquals(BookDetailsUiState(isLoading = false, book = book), awaitItem())
//
//            assertEquals(BookDetailsUiState(isLoading = false, book = book, error = null), awaitItem())
//
//            cancelAndIgnoreRemainingEvents()
//        }
//
//        bookDetailViewModel.fetchBookById("x2")
////        delay(500)
//
////        assert(bookDetailViewModel.bookState.value.book?.id == "x2")
//
//    }

    @Test
    fun `fetchBookById sets loading state initially`() = runTest {
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

        coEvery { getBookById.invoke("x1") } returns flowOf(book)
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(true)
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(true)


        bookDetailViewModel.fetchBookById("x1")

        bookDetailViewModel.bookState.test {
            assertEquals(BookDetailsUiState(isLoading = true), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchBookById updates state with book details on success`() = runTest {
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
        coEvery { getBookById.invoke("x1") } returns flowOf(book)
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(true)
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(true)


        bookDetailViewModel.fetchBookById("x1")

        bookDetailViewModel.bookState.test {
            assertEquals(BookDetailsUiState(isLoading = true), awaitItem())
            assertEquals(BookDetailsUiState(book = book), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

//    @Test
//    fun `fetchBookById updates state with error on failure`() = runTest {
//        val errorMessage = "Error fetching book"
//        coEvery { getBookById.invoke("x1") } throws Exception(errorMessage)
//
//
//        bookDetailViewModel.fetchBookById("x1")
//
//        bookDetailViewModel.bookState.test {
//            assertEquals(BookDetailsUiState(isLoading = true), awaitItem())
//            assertEquals(BookDetailsUiState(error = errorMessage), awaitItem())
//            cancelAndIgnoreRemainingEvents()
//        }
//    }

    @Test
    fun `fetchBookById checks wishlist and reading list for the book`() = runTest {
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
        coEvery { getBookById.invoke("x1") } returns flowOf(book)
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(true)
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(false)

        bookDetailViewModel.fetchBookById("x1")

        bookDetailViewModel.bookState.test {
            assertEquals(BookDetailsUiState(isLoading = true), awaitItem())
            assertEquals(BookDetailsUiState(book = book), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }

        bookDetailViewModel.checkReadinglistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `add to wishlist - addToWishlist`() = runTest {
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

        coJustRun { addIntoFavListUseCase.invoke(book) }
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(false)

        bookDetailViewModel.addToWishlist(book)

        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addToWishlist invokes use case and updates wishlist state`() = runTest {
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

        // Mock the behavior of the use case
        coJustRun { addIntoFavListUseCase.invoke(book) }
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(false)

        // Call the function
        bookDetailViewModel.addToWishlist(book)

        // Verify the state is updated correctly
        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `add to addToReadinglist - addToReadinglist`() = runTest {
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

        coJustRun { addIntoReadingListUseCase.invoke(book) }
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(false)

        bookDetailViewModel.addToReadinglist(book)

        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `addToReadinglist invokes use case and updates wishlist state`() = runTest {
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

        // Mock the behavior of the use case
        coJustRun { addIntoReadingListUseCase.invoke(book) }
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(false)

        // Call the function
        bookDetailViewModel.addToReadinglist(book)

        // Verify the state is updated correctly
        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeFromReadingList invokes use case and updates wishlist state`() = runTest {
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

        // Mock the behavior of the use case
        coJustRun { removeFromReadingListUseCase.invoke(book) }
        coEvery { isBookPresentInReadingListUseCase.invoke(book) } returns flowOf(false)

        // Call the function
        bookDetailViewModel.removeFromReadingList(book)

        // Verify the state is updated correctly
        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `removeFromWishList invokes use case and updates wishlist state`() = runTest {
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

        // Mock the behavior of the use case
        coJustRun { removeFromFavListUseCase.invoke(book) }
        coEvery { isBookPresentInFavListUserCase.invoke(book) } returns flowOf(false)

        // Call the function
        bookDetailViewModel.removeFromWishList(book)

        // Verify the state is updated correctly
        bookDetailViewModel.checkWishlistItem.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
