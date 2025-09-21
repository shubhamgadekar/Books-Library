package com.alpha.data.di

import com.alpha.data.local.dao.FavBookDao
import com.alpha.data.local.dao.ReadingListDao
import com.alpha.data.remote.BookApiService
import com.alpha.data.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    internal fun provideBookApi(retrofit: Retrofit): BookApiService {
        return retrofit.create(BookApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideBookRepository(
        api: BookApiService,
        favDao: FavBookDao,
        readingDao: ReadingListDao,
    ): BookRepository {
        return BookRepository(api, favDao, readingDao)
    }
}
