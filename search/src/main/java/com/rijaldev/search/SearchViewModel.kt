package com.rijaldev.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.usecase.MoviePagingUseCase
import com.rijaldev.core.domain.usecase.MovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    moviePagingUseCase: MoviePagingUseCase,
    private val movieUseCase: MovieUseCase
) : ViewModel() {
    val query = MutableStateFlow("")

    fun insertMovie(movie: Movie) = viewModelScope.launch {
        movieUseCase.insertMovie(movie)
    }

    val resultData = moviePagingUseCase.searchMoviesPaging(query)
        .cachedIn(viewModelScope)
        .asLiveData()
}