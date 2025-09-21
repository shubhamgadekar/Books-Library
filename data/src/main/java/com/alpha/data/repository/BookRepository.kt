package com.alpha.data.repository

import com.alpha.data.Book
import com.alpha.data.local.dao.FavBookDao
import com.alpha.data.local.dao.ReadingListDao
import com.alpha.data.mappers.toBook
import com.alpha.data.mappers.toBookEntity
import com.alpha.data.mappers.toReadingListEntity
import com.alpha.data.remote.BookApiService
import javax.inject.Inject

internal class BookRepository
@Inject constructor(
    private val api: BookApiService,
    private val localDao: FavBookDao,
    private val readingListDao: ReadingListDao,
) {
    suspend fun getBooks(query: String, start: Int = 0, count: Int = 20): List<Book> {
        val response = api.searchBooks(query, start, count)
        val books = response.items ?: emptyList()
        return books
    }

    suspend fun getBookById(id: String): Book {
        return api.getBookById(id)
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

    suspend fun isBookPresentInFavList(id: String): Boolean {
        val returnId = localDao.isBookPresent(id)
        return returnId.let {
            !(it == null || it.isEmpty())
        }
    }

    suspend fun isBookPresentInReadingList(id: String): Boolean {
        val returnId = readingListDao.isBookPresentInReadingList(id)
        return returnId.let {
            !(it == null || it.isEmpty())
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
