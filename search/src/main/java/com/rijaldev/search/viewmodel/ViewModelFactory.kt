package com.rijaldev.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rijaldev.core.domain.usecase.MoviePagingUseCase
import com.rijaldev.core.domain.usecase.MovieUseCase
import com.rijaldev.search.SearchViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val moviePagingUseCase: MoviePagingUseCase,
    private val movieUseCase: MovieUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(moviePagingUseCase, movieUseCase) as T
            else -> throw IllegalArgumentException("Unknown VieModel class name ${modelClass.name}")
        }
}