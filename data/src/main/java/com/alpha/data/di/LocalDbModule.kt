package com.alpha.data.di

import android.content.Context
import androidx.room.Room
import com.alpha.data.local.FavBookDatabase
import com.alpha.data.local.dao.FavBookDao
import com.alpha.data.local.dao.ReadingListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalDbModule {
    @Provides
    @Singleton
    internal fun provideLocalDatabase(
        @ApplicationContext context: Context,
    ): FavBookDatabase {
        val db =
            Room
                .databaseBuilder(context, FavBookDatabase::class.java, "fav_books_db")
                .build()
        return db
    }

    @Provides
    @Singleton
    internal fun provideBooksDao(db: FavBookDatabase): FavBookDao {
        return db.getFavBookDao()
    }

    @Provides
    @Singleton
    internal fun provideReadingListDao(db: FavBookDatabase): ReadingListDao {
        return db.getReadingListDao()
    }
}
