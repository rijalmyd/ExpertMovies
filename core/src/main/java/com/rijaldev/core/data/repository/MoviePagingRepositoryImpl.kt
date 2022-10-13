package com.rijaldev.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rijaldev.core.data.paging.MoviePagingSource
import com.rijaldev.core.data.source.remote.retrofit.ApiService
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.repository.MoviePagingRepository
import com.rijaldev.core.utils.DataMapper
import com.rijaldev.core.domain.model.MovieType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviePagingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MoviePagingRepository {

    override fun getMoviesPaging(movieType: MovieType): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 8),
            pagingSourceFactory = {
                MoviePagingSource(apiService, movieType)
            }
        ).flow.map {
            DataMapper.mapPagingResponseToPagingDomain(it, movieType)
        }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun searchMoviesPaging(query: StateFlow<String>): Flow<PagingData<Movie>> =
        query.debounce(300)
            .filter { it.trim().isNotEmpty() }
            .distinctUntilChanged()
            .flatMapLatest {
                Pager(
                    config = PagingConfig(pageSize = 10),
                    pagingSourceFactory = {
                        MoviePagingSource(apiService, MovieType.SEARCH, it)
                    }
                ).flow.map {
                    DataMapper.mapPagingResponseToPagingDomain(it, MovieType.SEARCH)
                }
            }
}