package com.alpha.books_explorer.di

import com.alpha.books_explorer.data.local.dao.FavBookDao
import com.alpha.books_explorer.data.local.dao.ReadingListDao
import com.alpha.books_explorer.data.remote.BookApiService
import com.alpha.books_explorer.data.repository.BookRepositoryImpl
import com.alpha.books_explorer.domain.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideBookApi(retrofit: Retrofit): BookApiService {
        return retrofit.create(BookApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookRepository(
        api: BookApiService, favDao: FavBookDao, readingDao: ReadingListDao
    ): BookRepository {
        return BookRepositoryImpl(api, favDao, readingDao)
    }
}
