package com.alpha.data.remote

import com.alpha.data.Book
import com.alpha.data.remote.dto.BookSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q")
        query: String,
        @Query("startIndex")
        startIndex: Int,
        @Query("maxResults")
        maxResults: Int,
    ): BookSearchResponse

    @GET("volumes/{bookId}")
    suspend fun getBookById(
        @Path("bookId") bookId: String,
        @Query("key") key: String = "",
    ): Book
}
