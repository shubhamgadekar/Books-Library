package com.alpha.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpha.data.Book
import com.alpha.data.remote.BookApiService

internal class BooksPagingSource(
    private val api: BookApiService,
    private val query: String,
) : PagingSource<Int, Book>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        val position = params.key ?: 0
        val response =
            api.searchBooks(
                query = query,
                startIndex = position,
                maxResults = params.loadSize,
            )

        val books = response.items ?: emptyList()

        return LoadResult.Page(
            data = books,
            prevKey = if (position == 0) null else position - params.loadSize,
            nextKey = if (books.isEmpty()) null else position + params.loadSize,
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
    }
}
