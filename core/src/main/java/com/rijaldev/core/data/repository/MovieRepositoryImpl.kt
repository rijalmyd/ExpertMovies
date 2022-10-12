package com.rijaldev.core.data.repository

import com.rijaldev.core.data.source.local.LocalDataSource
import com.rijaldev.core.data.source.remote.RemoteDataSource
import com.rijaldev.core.data.source.remote.response.MovieItem
import com.rijaldev.core.data.source.remote.retrofit.ApiResponse
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.model.Video
import com.rijaldev.core.domain.repository.MovieRepository
import com.rijaldev.core.utils.DataMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> =
                local.getMovies(MovieType.TRENDING.name).map {
                    DataMapper.mapMovieEntitiesToMovieDomain(it)
                }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> =
                remote.getTrendingMovies()

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapMovieResponseToEntity(data, MovieType.TRENDING)
                local.insertMovies(entity)
            }
        }.asFlow()

    override fun getMoviesNowPlaying(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> =
                local.getMovies(MovieType.NOW_PLAYING.name).map {
                    DataMapper.mapMovieEntitiesToMovieDomain(it)
                }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> =
                remote.getMoviesNowPlaying()

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapMovieResponseToEntity(data, MovieType.NOW_PLAYING)
                local.insertMovies(entity)
            }
        }.asFlow()

    override fun getPopularMovies(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> =
                local.getMovies(MovieType.POPULAR.name).map {
                    DataMapper.mapMovieEntitiesToMovieDomain(it)
                }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> =
                remote.getPopularMovies()

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapMovieResponseToEntity(data, MovieType.POPULAR)
                local.insertMovies(entity)
            }
        }.asFlow()

    override fun getUpcomingMovies(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> =
                local.getMovies(MovieType.UPCOMING.name).map {
                    DataMapper.mapMovieEntitiesToMovieDomain(it)
                }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> =
                remote.getUpcomingMovies()

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapMovieResponseToEntity(data, MovieType.UPCOMING)
                local.insertMovies(entity)
            }
        }.asFlow()

    override fun getTopRatedMovies(): Flow<Resource<List<Movie>>> =
        object : NetworkBoundResource<List<Movie>, List<MovieItem>>() {
            override fun loadFromDB(): Flow<List<Movie>> =
                local.getMovies(MovieType.TOP_RATED.name).map {
                    DataMapper.mapMovieEntitiesToMovieDomain(it)
                }

            override fun shouldFetch(data: List<Movie>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<MovieItem>>> =
                remote.getTopRatedMovies()

            override suspend fun saveCallResult(data: List<MovieItem>) {
                val entity = DataMapper.mapMovieResponseToEntity(data, MovieType.TOP_RATED)
                local.insertMovies(entity)
            }
        }.asFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun searchMovies(query: StateFlow<String>): Flow<Resource<List<Movie>>> =
        flow {
            emit(Resource.Loading())
            try {
                val result = query.debounce(300)
                    .distinctUntilChanged()
                    .filter { it.trim().isNotEmpty() }
                    .mapLatest {
                        remote.searchMovies(it).results
                    }.map {
                        Resource.Success(DataMapper.mapMovieResponseToDomain(it, MovieType.SEARCH))
                    }
                emitAll(result)
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override fun getVideos(movieId: Int): Flow<Resource<List<Video>?>> =
        flow {
            try {
                val response = remote.getVideos(movieId)
                val result = DataMapper.mapVideoResponseToDomain(response.results)
                if (!result.isNullOrEmpty()) emit(Resource.Success(result))
                else emit(Resource.Error("No Videos Found"))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    override suspend fun insertMovie(movie: Movie) {
        val data = DataMapper.mapMovieDomainToEntity(movie)
        local.insertMovie(data)
    }

    override fun isFavoriteMovie(movieId: Int): Flow<Boolean> =
        local.isFavoriteMovie(movieId)

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        local.getFavoriteMovies().map {
            DataMapper.mapMovieEntitiesToMovieDomain(it)
        }

    override suspend fun updateMovie(movieId: Int, isFavorite: Boolean) =
        local.updateMovie(movieId, isFavorite)
}