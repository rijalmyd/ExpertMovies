package com.rijaldev.core.domain.usecase

import com.rijaldev.core.domain.repository.MovieRepository
import javax.inject.Inject

class FavoriteMovieInteractor @Inject constructor(private val movieRepository: MovieRepository) : FavoriteMovieUseCase {
    override fun getFavoriteMovies() = movieRepository.getFavoriteMovies()

    override fun isFavoriteMovie(movieId: Int) = movieRepository.isFavoriteMovie(movieId)

    override suspend fun updateMovie(movieId: Int, isFavorite: Boolean) = movieRepository.updateMovie(movieId, isFavorite)
}