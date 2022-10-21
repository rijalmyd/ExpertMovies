package com.rijaldev.expertmovies.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rijaldev.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    val trendingMovies by lazy {
        movieUseCase.getTrendingMovies().asLiveData()
    }

    val nowPlayingMovies by lazy {
        movieUseCase.getMoviesNowPlaying().asLiveData()
    }

    val popularMovies by lazy {
        movieUseCase.getPopularMovies().asLiveData()
    }

    val upComingMovies by lazy {
        movieUseCase.getUpcomingMovies().asLiveData()
    }

    val topRatedMovies by lazy {
        movieUseCase.getTopRatedMovies().asLiveData()
    }
}