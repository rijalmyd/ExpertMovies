package com.rijaldev.expertmovies.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rijaldev.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    val trendingMovies = movieUseCase.getTrendingMovies().asLiveData()

    val nowPlayingMovies = movieUseCase.getMoviesNowPlaying().asLiveData()

    val popularMovies = movieUseCase.getPopularMovies().asLiveData()

    val upComingMovies = movieUseCase.getUpcomingMovies().asLiveData()

    val topRatedMovies = movieUseCase.getTopRatedMovies().asLiveData()
}