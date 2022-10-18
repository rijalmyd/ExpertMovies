package com.rijaldev.expertmovies.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.repository.MovieRepository
import com.rijaldev.core.domain.usecase.FavoriteMovieInteractor
import com.rijaldev.core.domain.usecase.FavoriteMovieUseCase
import com.rijaldev.core.domain.usecase.MovieInteractor
import com.rijaldev.core.domain.usecase.MovieUseCase
import com.rijaldev.expertmovies.utils.DataDummy
import com.rijaldev.expertmovies.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.expertmovies.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock private lateinit var movieRepository: MovieRepository
    private lateinit var favoriteMovieUseCase: FavoriteMovieUseCase
    private lateinit var movieUseCase: MovieUseCase
    private lateinit var detailViewModel: DetailViewModel
    private val dummyMovie = DataDummy.getDummyMovies()[0]

    @Before
    fun setUp() {
        favoriteMovieUseCase = FavoriteMovieInteractor(movieRepository)
        movieUseCase = MovieInteractor(movieRepository)
        detailViewModel = DetailViewModel(favoriteMovieUseCase, movieUseCase)
    }

    @Test
    fun `when Update Should Called In The Repository`() = runTest {
        val isFavorite = !dummyMovie.isFavorite
        detailViewModel.update(dummyMovie.id, isFavorite)
        Mockito.verify(movieRepository).updateMovie(dummyMovie.id, isFavorite)
    }

    @Test
    fun `when isFavoriteMovie Should Return True`() = runTest {
        val isFavorite = true

        `when`(favoriteMovieUseCase.isFavoriteMovie(dummyMovie.id)).thenReturn(flowOf(isFavorite))

        val actualValue = detailViewModel.isFavoriteMovie(dummyMovie.id).getOrAwaitValue()
        Mockito.verify(movieRepository).isFavoriteMovie(dummyMovie.id)
        assertNotNull(actualValue)
        assertTrue(actualValue)
        assertEquals(isFavorite, actualValue)
    }

    @Test
    fun `when isFavoriteMovie Should Return False`() = runTest {
        val isFavorite = false

        `when`(favoriteMovieUseCase.isFavoriteMovie(dummyMovie.id)).thenReturn(flowOf(isFavorite))

        val actualValue = detailViewModel.isFavoriteMovie(dummyMovie.id).getOrAwaitValue()
        Mockito.verify(movieRepository).isFavoriteMovie(dummyMovie.id)
        assertNotNull(actualValue)
        assertFalse(actualValue)
        assertEquals(isFavorite, actualValue)
    }

    @Test
    fun `when getVideos Should Return Success`() = runTest {
        val dummyVideos = DataDummy.getDummyVideos()

        `when`(movieUseCase.getVideos(dummyMovie.id)).thenReturn(flowOf(Resource.Success(dummyVideos)))

        val actualVideos = detailViewModel.getVideos(dummyMovie.id).getOrAwaitValue()
        Mockito.verify(movieRepository).getVideos(dummyMovie.id)
        assertNotNull(actualVideos)
        assertTrue(actualVideos is Resource.Success)
        assertEquals(dummyVideos.size, (actualVideos as Resource.Success).data?.size)
    }

    @Test
    fun `when getVideos Should Return Error`() = runTest {
        `when`(movieUseCase.getVideos(dummyMovie.id)).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualVideos = detailViewModel.getVideos(dummyMovie.id).getOrAwaitValue()
        Mockito.verify(movieRepository).getVideos(dummyMovie.id)
        assertNotNull(actualVideos)
        assertTrue(actualVideos is Resource.Error)
        assertEquals(ERROR_MSG, (actualVideos as Resource.Error).message)
    }

    private companion object {
        const val ERROR_MSG = "Error"
    }
}