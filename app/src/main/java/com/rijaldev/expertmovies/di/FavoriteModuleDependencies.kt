package com.rijaldev.expertmovies.di

import com.rijaldev.core.domain.usecase.FavoriteMovieUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {
    fun provideFavoriteMovieUseCase(): FavoriteMovieUseCase
}