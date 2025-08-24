package com.alpha.books_explorer.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpha.books_explorer.data.remote.BookApiService
import com.alpha.books_explorer.domain.model.Book

class BooksPagingSource(
    private val api: BookApiService,
    private val query: String,
) : PagingSource<Int, Book>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            val position = params.key ?: 0
            val response =
                api.searchBooks(
                    query = query,
                    startIndex = position,
                    maxResults = params.loadSize,
                )

            val books = response.items ?: emptyList()

            LoadResult.Page(
                data = books,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (books.isEmpty()) null else position + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
    }
}
