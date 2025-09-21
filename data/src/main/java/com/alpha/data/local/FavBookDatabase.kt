package com.alpha.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpha.data.local.converters.Converters
import com.alpha.data.local.dao.FavBookDao
import com.alpha.data.local.dao.ReadingListDao
import com.alpha.data.local.entities.BookEntity
import com.alpha.data.local.entities.ReadingListEntity

@Database(entities = [BookEntity::class, ReadingListEntity::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class FavBookDatabase : RoomDatabase() {
    abstract fun getFavBookDao(): FavBookDao

    abstract fun getReadingListDao(): ReadingListDao
}
