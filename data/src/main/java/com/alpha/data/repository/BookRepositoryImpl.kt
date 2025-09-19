package com.alpha.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alpha.data.Book
import com.alpha.data.local.dao.FavBookDao
import com.alpha.data.local.dao.ReadingListDao
import com.alpha.data.mappers.toBook
import com.alpha.data.mappers.toBookEntity
import com.alpha.data.mappers.toReadingListEntity
import com.alpha.data.paging.BooksPagingSource
import com.alpha.data.remote.BookApiService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class BookRepositoryImpl
@Inject constructor(
    private val api: BookApiService,
    private val localDao: FavBookDao,
    private val readingListDao: ReadingListDao,
) {
    suspend fun getBooks(query: String, start: Int = 0, count: Int = 10): List<Book> {
        val response = api.searchBooks(query, start, count)
        val books = response.items ?: emptyList()
        return books
    }

    fun getBooksFromPaging(query: String): Flow<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = 20,
                pageSize = 20,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { BooksPagingSource(api, query) },
        ).flow
    }

    suspend fun getBookById(id: String): Book {
        return api.getBookById(id)
//        emit(response)
    }

    suspend fun addIntoFavListBooks(book: Book) {
        localDao.insertFavBook(book.toBookEntity())
    }

    suspend fun addIntoReadingListBooks(book: Book) {
        readingListDao.insertIntoReadingList(book.toReadingListEntity())
    }

    suspend fun deleteFromFavListBooks(book: Book) {
        localDao.deleteBook(book.toBookEntity())
    }

    suspend fun deleteFromReadingListBooks(book: Book) {
        readingListDao.deleteFromReadingList(book.toReadingListEntity())
    }

    fun isBookPresentInFavList(book: Book): Flow<Boolean> = flow {
        val returnId = localDao.isBookPresent(book.id)
        returnId.let {
            if (it == null || it.isEmpty()) {
                emit(false)
            } else {
                emit(true)
            }
        }
    }

    fun isBookPresentInReadingList(book: Book): Flow<Boolean> = flow {
        val returnId = readingListDao.isBookPresentInReadingList(book.id)
        returnId.let {
            if (it == null || it.isEmpty()) {
                emit(false)
            } else {
                emit(true)
            }
        }
    }

    suspend fun getFavListBooks(): List<Book> {
        return localDao.getFavBooks().map {
            it.toBook()
        }
    }

    suspend fun getReadingListBooks(): List<Book> {
        return readingListDao.getReadingListBooks().map {
            it.toBook()
        }
    }
}
