package com.alpha.books_explorer.data.remote

import com.alpha.books_explorer.data.remote.dto.BookSearchResponse
import com.alpha.books_explorer.domain.model.Book
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BookApiService {

    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("startIndex") startIndex: Int = 0,
        @Query("maxResults") maxResults: Int = 10
    ): BookSearchResponse

    @GET("volumes/{bookId}")
    suspend fun getBookById(
        @Path("bookId") bookId: String,
        @Query("key") key: String = ""
    ): Book

}
