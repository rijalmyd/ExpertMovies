package com.rijaldev.expertmovies.ui.main.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.usecase.MoviePagingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviePagingUseCase: MoviePagingUseCase
) : ViewModel() {
    fun getMoviesPaging(movieType: MovieType) = moviePagingUseCase.getMoviesPaging(movieType)
        .cachedIn(viewModelScope)
        .asLiveData()
}