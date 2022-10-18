package com.rijaldev.expertmovies.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rijaldev.core.domain.usecase.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    fun trendingMovies() = movieUseCase.getTrendingMovies().asLiveData()

    fun nowPlayingMovies() = movieUseCase.getMoviesNowPlaying().asLiveData()

    fun popularMovies() = movieUseCase.getPopularMovies().asLiveData()

    fun upComingMovies() = movieUseCase.getUpcomingMovies().asLiveData()

    fun topRatedMovies() = movieUseCase.getTopRatedMovies().asLiveData()
}