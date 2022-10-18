package com.rijaldev.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rijaldev.core.domain.usecase.FavoriteMovieUseCase

class FavoriteViewModel(private val favoriteMovieUseCase: FavoriteMovieUseCase) : ViewModel() {

    fun favoriteMovies() = favoriteMovieUseCase.getFavoriteMovies().asLiveData()
}