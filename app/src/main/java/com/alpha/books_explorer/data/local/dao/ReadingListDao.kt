package com.alpha.books_explorer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alpha.books_explorer.data.local.entities.ReadingListEntity

@Dao
interface ReadingListDao {
    @Query("SELECT * FROM readingList")
    suspend fun getReadingListBooks(): List<ReadingListEntity>

    @Query("SELECT * FROM readingList WHERE id = :id LIMIT 1")
    suspend fun getBookById(id: String): ReadingListEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoReadingList(book: ReadingListEntity)

    @Delete
    suspend fun deleteFromReadingList(book: ReadingListEntity)

    @Query("SELECT id FROM readingList WHERE id = :id LIMIT 1")
    suspend fun isBookPresentInReadingList(id: String): String?
}
