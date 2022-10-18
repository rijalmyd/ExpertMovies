package com.rijaldev.expertmovies.utils

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rijaldev.core.domain.model.Movie

class MoviePagingSource : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int  = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapshot(items: List<Movie>): PagingData<Movie> {
            return PagingData.from(items)
        }
    }
}