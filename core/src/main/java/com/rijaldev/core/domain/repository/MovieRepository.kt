package com.rijaldev.core.domain.repository

import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MovieRepository {

    fun getTrendingMovies(): Flow<Resource<List<Movie>>>

    fun getMoviesNowPlaying(): Flow<Resource<List<Movie>>>

    fun getPopularMovies(): Flow<Resource<List<Movie>>>

    fun getUpcomingMovies(): Flow<Resource<List<Movie>>>

    fun getTopRatedMovies(): Flow<Resource<List<Movie>>>

    fun searchMovies(query: StateFlow<String>): Flow<Resource<List<Movie>>>

    fun getVideos(movieId: Int): Flow<Resource<List<Video>?>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    fun isFavoriteMovie(movieId: Int): Flow<Boolean>

    suspend fun insertMovie(movie: Movie)

    suspend fun updateMovie(movieId: Int, isFavorite: Boolean)
}