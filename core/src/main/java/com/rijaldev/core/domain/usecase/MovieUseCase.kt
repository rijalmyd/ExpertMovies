package com.rijaldev.core.domain.usecase

import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getTrendingMovies(): Flow<Resource<List<Movie>>>

    fun getMoviesNowPlaying(): Flow<Resource<List<Movie>>>

    fun getPopularMovies(): Flow<Resource<List<Movie>>>

    fun getUpcomingMovies(): Flow<Resource<List<Movie>>>

    fun getTopRatedMovies(): Flow<Resource<List<Movie>>>

    fun getVideos(movieId: Int): Flow<Resource<List<Video>?>>

    suspend fun insertMovie(movie: Movie)
}