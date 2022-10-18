package com.rijaldev.expertmovies.ui.main.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.repository.MovieRepository
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
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieUseCase: MovieUseCase
    private lateinit var homeViewModel: HomeViewModel
    private val dummyMovies = DataDummy.getDummyMovies()

    @Before
    fun setUp() {
        movieUseCase = MovieInteractor(movieRepository)
        homeViewModel = HomeViewModel(movieUseCase)
    }

    @Test
    fun `when Get Trending Movies Should Not Null and Return Success`() = runTest {
        `when`(movieUseCase.getTrendingMovies()).thenReturn(flowOf(Resource.Success(dummyMovies)))

        val actualMovie = homeViewModel.trendingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getTrendingMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Success)
        assertEquals(dummyMovies.size, (actualMovie as Resource.Success).data?.size)
    }

    @Test
    fun `when Get Trending Movies Should Not Null and Return Error`() = runTest {
        `when`(movieUseCase.getTrendingMovies()).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualMovie = homeViewModel.trendingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getTrendingMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Error)
        assertEquals(ERROR_MSG, (actualMovie as Resource.Error).message)
    }

    @Test
    fun `when Get Now Playing Movies Should Not Null and Return Success`() = runTest {
        `when`(movieUseCase.getMoviesNowPlaying()).thenReturn(flowOf(Resource.Success(dummyMovies)))

        val actualMovie = homeViewModel.nowPlayingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getMoviesNowPlaying()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Success)
        assertEquals(dummyMovies.size, (actualMovie as Resource.Success).data?.size)
    }

    @Test
    fun `when Get Now Playing Movies Should Not Null and Return Error`() = runTest {
        `when`(movieUseCase.getMoviesNowPlaying()).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualMovie = homeViewModel.nowPlayingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getMoviesNowPlaying()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Error)
        assertEquals(ERROR_MSG, (actualMovie as Resource.Error).message)
    }

    @Test
    fun `when Get Popular Movies Should Not Null and Return Success`() = runTest {
        `when`(movieUseCase.getPopularMovies()).thenReturn(flowOf(Resource.Success(dummyMovies)))

        val actualMovie = homeViewModel.popularMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getPopularMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Success)
        assertEquals(dummyMovies.size, (actualMovie as Resource.Success).data?.size)
    }

    @Test
    fun `when Get Popular Movies Should Not Null and Return Error`() = runTest {
        `when`(movieUseCase.getPopularMovies()).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualMovie = homeViewModel.popularMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getPopularMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Error)
        assertEquals(ERROR_MSG, (actualMovie as Resource.Error).message)
    }

    @Test
    fun `when Get Upcoming Movies Should Not Null and Return Success`() = runTest {
        `when`(movieUseCase.getUpcomingMovies()).thenReturn(flowOf(Resource.Success(dummyMovies)))

        val actualMovie = homeViewModel.upComingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getUpcomingMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Success)
        assertEquals(dummyMovies.size, (actualMovie as Resource.Success).data?.size)
    }

    @Test
    fun `when Get Upcoming Movies Should Not Null and Return Error`() = runTest {
        `when`(movieUseCase.getUpcomingMovies()).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualMovie = homeViewModel.upComingMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getUpcomingMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Error)
        assertEquals(ERROR_MSG, (actualMovie as Resource.Error).message)
    }

    @Test
    fun `when Get Top Rated Movies Should Not Null and Return Success`() = runTest {
        `when`(movieUseCase.getTopRatedMovies()).thenReturn(flowOf(Resource.Success(dummyMovies)))

        val actualMovie = homeViewModel.topRatedMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getTopRatedMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Success)
        assertEquals(dummyMovies.size, (actualMovie as Resource.Success).data?.size)
    }

    @Test
    fun `when Get Top Rated Movies Should Not Null and Return Error`() = runTest {
        `when`(movieUseCase.getTopRatedMovies()).thenReturn(flowOf(Resource.Error(ERROR_MSG)))

        val actualMovie = homeViewModel.topRatedMovies().getOrAwaitValue()
        Mockito.verify(movieRepository).getTopRatedMovies()
        assertNotNull(actualMovie)
        assertTrue(actualMovie is Resource.Error)
        assertEquals(ERROR_MSG, (actualMovie as Resource.Error).message)
    }

    private companion object {
        const val ERROR_MSG = "Error"
    }
}