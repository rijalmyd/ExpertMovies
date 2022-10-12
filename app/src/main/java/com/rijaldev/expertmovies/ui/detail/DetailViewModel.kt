package com.rijaldev.expertmovies.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rijaldev.core.domain.usecase.FavoriteMovieUseCase
import com.rijaldev.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val favoriteMovieUseCase: FavoriteMovieUseCase,
    private val movieUseCase: MovieUseCase
) : ViewModel() {

    fun update(movieId: Int, isFavorite: Boolean) = viewModelScope.launch {
        favoriteMovieUseCase.updateMovie(movieId, isFavorite)
    }

    fun isFavoriteMovie(movieId: Int) = favoriteMovieUseCase.isFavoriteMovie(movieId).asLiveData()

    fun getVideos(movieId: Int) = movieUseCase.getVideos(movieId).asLiveData()
}