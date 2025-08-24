package com.alpha.books_explorer.di

import android.content.Context
import androidx.room.Room
import com.alpha.books_explorer.data.local.FavBookDatabase
import com.alpha.books_explorer.data.local.dao.FavBookDao
import com.alpha.books_explorer.data.local.dao.ReadingListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDbModule {

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): FavBookDatabase {
        val db = Room
            .databaseBuilder(context, FavBookDatabase::class.java, "fav_books_db")
            .build()
        return db
    }

    @Provides
    @Singleton
    fun provideBooksDao(db: FavBookDatabase): FavBookDao {
        return db.getFavBookDao()
    }

    @Provides
    @Singleton
    fun provideReadingListDao(db: FavBookDatabase): ReadingListDao {
        return db.getReadingListDao()
    }
}
