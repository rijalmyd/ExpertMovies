package com.rijaldev.core.domain.usecase

import androidx.paging.PagingData
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MoviePagingUseCase {
    fun getMoviesPaging(movieType: MovieType): Flow<PagingData<Movie>>

    fun searchMoviesPaging(query: StateFlow<String>): Flow<PagingData<Movie>>
}