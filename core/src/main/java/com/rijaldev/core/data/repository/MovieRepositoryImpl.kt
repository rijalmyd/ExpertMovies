package com.rijaldev.core.data.repository

import com.rijaldev.core.data.source.local.LocalDataSource
import com.rijaldev.core.data.source.remote.RemoteDataSource
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.model.Video
import com.rijaldev.core.domain.repository.MovieRepository
import com.rijaldev.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource,
    private val local: LocalDataSource
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Resource<List<Movie>>> = networkBoundResource(
        query = {
            local.getMovies(MovieType.TRENDING.name).map {
                DataMapper.mapMovieEntitiesToMovieDomain(it)
            }
        },
        fetch = {
            remote.getTrendingMovies()
        },
        saveFetchResult = {
            val entity = DataMapper.mapMovieResponseToEntity(it, MovieType.TRENDING)
            local.insertMovies(entity)
        },
        shouldFetch = {
            it == null || it.isEmpty()
        }
    )

    override fun getMoviesNowPlaying(): Flow<Resource<List<Movie>>> = networkBoundResource(
        query = {
            local.getMovies(MovieType.NOW_PLAYING.name).map {
                DataMapper.mapMovieEntitiesToMovieDomain(it)
            }
        },
        fetch = {
            remote.getMoviesNowPlaying()
        },
        saveFetchResult = {
            val entity = DataMapper.mapMovieResponseToEntity(it, MovieType.NOW_PLAYING)
            local.insertMovies(entity)
        },
        shouldFetch = {
            it == null || it.isEmpty()
        }
    )

    override fun getPopularMovies(): Flow<Resource<List<Movie>>> = networkBoundResource(
        query = {
            local.getMovies(MovieType.POPULAR.name).map {
                DataMapper.mapMovieEntitiesToMovieDomain(it)
            }
        },
        fetch = {
            remote.getPopularMovies()
        },
        saveFetchResult = {
            val entity = DataMapper.mapMovieResponseToEntity(it, MovieType.POPULAR)
            local.insertMovies(entity)
        },
        shouldFetch = {
            it == null || it.isEmpty()
        }
    )

    override fun getUpcomingMovies(): Flow<Resource<List<Movie>>> = networkBoundResource(
        query = {
            local.getMovies(MovieType.UPCOMING.name).map {
                DataMapper.mapMovieEntitiesToMovieDomain(it)
            }
        },
        fetch = {
            remote.getUpcomingMovies()
        },
        saveFetchResult = {
            val entity = DataMapper.mapMovieResponseToEntity(it, MovieType.UPCOMING)
            local.insertMovies(entity)
        },
        shouldFetch = {
            it == null || it.isEmpty()
        }
    )

    override fun getTopRatedMovies(): Flow<Resource<List<Movie>>> = networkBoundResource(
        query = {
            local.getMovies(MovieType.TOP_RATED.name).map {
                DataMapper.mapMovieEntitiesToMovieDomain(it)
            }
        },
        fetch = {
            remote.getTopRatedMovies()
        },
        saveFetchResult = {
            val entity = DataMapper.mapMovieResponseToEntity(it, MovieType.TOP_RATED)
            local.insertMovies(entity)
        },
        shouldFetch = {
            it == null || it.isEmpty()
        }
    )

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