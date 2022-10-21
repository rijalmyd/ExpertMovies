package com.rijaldev.expertmovies.ui.main.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.repository.MoviePagingRepository
import com.rijaldev.core.domain.usecase.MoviePagingInteractor
import com.rijaldev.core.domain.usecase.MoviePagingUseCase
import com.rijaldev.core.ui.adapter.MovieAdapter
import com.rijaldev.expertmovies.utils.DataDummy
import com.rijaldev.expertmovies.utils.LiveDataTestUtil.getOrAwaitValue
import com.rijaldev.expertmovies.utils.MainDispatcherRule
import com.rijaldev.expertmovies.utils.MoviePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var moviePagingRepository: MoviePagingRepository
    private lateinit var moviePagingUseCase: MoviePagingUseCase
    private lateinit var moviesViewModel: MoviesViewModel
    private val dummyMovies = DataDummy.getDummyMovies()

    @Before
    fun setUp() {
        moviePagingUseCase = MoviePagingInteractor(moviePagingRepository)
        moviesViewModel = MoviesViewModel(moviePagingUseCase)
    }

    @Test
    fun `when Get Movies Paging Should Not Null and Return Success`() = runTest {
        val movieType = MovieType.NOW_PLAYING
        val data = MoviePagingSource.snapshot(dummyMovies)

        `when`(moviePagingUseCase.getMoviesPaging(movieType)).thenReturn(flowOf(data))

        moviesViewModel.movieType.value = MovieType.NOW_PLAYING
        val actualMovies = moviesViewModel.getMoviesPaging.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MovieAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualMovies)
        assertNotNull(differ.snapshot())
        assertEquals(dummyMovies, differ.snapshot())
        assertEquals(dummyMovies.size, differ.snapshot().size)
        assertEquals(dummyMovies[0].movieType, differ.snapshot()[0]?.movieType)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}