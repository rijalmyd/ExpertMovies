package com.rijaldev.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rijaldev.core.domain.usecase.FavoriteMovieUseCase

class FavoriteViewModel(favoriteMovieUseCase: FavoriteMovieUseCase) : ViewModel() {

    val favoriteMovies = favoriteMovieUseCase.getFavoriteMovies().asLiveData()
}