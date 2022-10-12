package com.rijaldev.expertmovies.di

import com.rijaldev.core.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideFavoriteMovieUseCase(favoriteMovieInteractor: FavoriteMovieInteractor): FavoriteMovieUseCase

    @Binds
    @Singleton
    abstract fun provideMovieUseCase(movieInteractor: MovieInteractor): MovieUseCase

    @Binds
    @Singleton
    abstract fun provideMoviePagingUseCase(moviePagingInteractor: MoviePagingInteractor): MoviePagingUseCase
}