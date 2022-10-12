package com.rijaldev.expertmovies.di

import com.rijaldev.core.domain.usecase.MoviePagingUseCase
import com.rijaldev.core.domain.usecase.MovieUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SearchModuleDependencies {
    fun provideMovieUseCase(): MovieUseCase

    fun provideMoviePagingUseCase(): MoviePagingUseCase
}