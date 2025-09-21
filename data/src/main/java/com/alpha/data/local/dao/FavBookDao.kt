package com.alpha.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alpha.data.local.entities.BookEntity

@Dao
internal interface FavBookDao {
    @Query("SELECT * FROM favBooks")
    suspend fun getFavBooks(): List<BookEntity>

    @Query("SELECT * FROM favBooks WHERE id = :id LIMIT 1")
    suspend fun getFavBookById(id: String): BookEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("SELECT id FROM favBooks WHERE id = :id LIMIT 1")
    suspend fun isBookPresent(id: String): String?
}
