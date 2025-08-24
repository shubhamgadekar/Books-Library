package com.alpha.books_explorer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alpha.books_explorer.data.local.dao.FavBookDao
import com.alpha.books_explorer.data.local.dao.ReadingListDao
import com.alpha.books_explorer.data.mappers.toBook
import com.alpha.books_explorer.data.mappers.toBookEntity
import com.alpha.books_explorer.data.mappers.toReadingListEntity
import com.alpha.books_explorer.data.paging.BooksPagingSource
import com.alpha.books_explorer.data.remote.BookApiService
import com.alpha.books_explorer.domain.model.Book
import com.alpha.books_explorer.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.text.isEmpty


class BookRepositoryImpl @Inject constructor(
    private val api: BookApiService,
    private val localDao: FavBookDao,
    private val readingListDao: ReadingListDao
) : BookRepository {
    override fun getBooks(query: String): Flow<List<Book>> = flow {
        val response = api.searchBooks(query)
        val books = response.items ?: emptyList()
        emit(books)
    }

    override fun getBooksFromPaging(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 20,
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BooksPagingSource(api, query) }
        ).flow
    }

    override fun getBookById(id: String): Flow<Book> = flow {
        val response = api.getBookById(id)
        emit(response)
    }

    override suspend fun addIntoFavListBooks(book: Book) {
        localDao.insertFavBook(book.toBookEntity())
    }

    override suspend fun addIntoReadingListBooks(book: Book) {
        readingListDao.insertIntoReadingList(book.toReadingListEntity())
    }

    override suspend fun deleteFromFavListBooks(book: Book) {
        localDao.deleteBook(book.toBookEntity())
    }

    override suspend fun deleteFromReadingListBooks(book: Book) {
        readingListDao.deleteFromReadingList(book.toReadingListEntity())
    }

    override fun isBookPresentInFavList(book: Book): Flow<Boolean> = flow {
        val returnId = localDao.isBookPresent(book.id)
        returnId.let {
            if (it == null || it.isEmpty()) {
                emit(false)
            } else {
                emit(true)
            }
        }
    }

    override fun isBookPresentInReadingList(book: Book): Flow<Boolean> = flow {
        val returnId = readingListDao.isBookPresentInReadingList(book.id)
        returnId.let {
            if (it == null || it.isEmpty()) {
                emit(false)
            } else {
                emit(true)
            }
        }
    }

    override fun getFavListBooks(): Flow<List<Book>> = flow {
        emit(
            localDao.getFavBooks().map {
                it.toBook()
            }
        )
    }

    override fun getReadingListBooks(): Flow<List<Book>> = flow {
        emit(
            readingListDao.getReadingListBooks().map {
                it.toBook()
            }
        )
    }
}
