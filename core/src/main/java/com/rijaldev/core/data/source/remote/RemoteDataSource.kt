package com.rijaldev.core.data.source.remote

import com.rijaldev.core.BuildConfig
import com.rijaldev.core.data.source.remote.response.MovieItem
import com.rijaldev.core.data.source.remote.retrofit.ApiResponse
import com.rijaldev.core.data.source.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getTrendingMovies(): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getTrendingMovies(BuildConfig.API_KEY)
                val data = response.results
                if (data.isNotEmpty()) emit(ApiResponse.Success(data))
                else emit(ApiResponse.Empty)
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getMoviesNowPlaying(): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getMoviesNowPlaying(BuildConfig.API_KEY)
                val data = response.results
                if (data.isNotEmpty()) emit(ApiResponse.Success(data))
                else emit(ApiResponse.Empty)
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getPopularMovies(): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getPopularMovies(BuildConfig.API_KEY)
                val data = response.results
                if (data.isNotEmpty()) emit(ApiResponse.Success(data))
                else emit(ApiResponse.Empty)
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getUpcomingMovies(): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getUpcomingMovies(BuildConfig.API_KEY)
                val data = response.results
                if (data.isNotEmpty()) emit(ApiResponse.Success(data))
                else emit(ApiResponse.Empty)
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getTopRatedMovies(): Flow<ApiResponse<List<MovieItem>>> =
        flow {
            try {
                val response = apiService.getTopRatedMovies(BuildConfig.API_KEY)
                val data = response.results
                if (data.isNotEmpty()) emit(ApiResponse.Success(data))
                else emit(ApiResponse.Empty)
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun searchMovies(query: String) = apiService.searchMovies(BuildConfig.API_KEY, query)

    suspend fun getVideos(movieId: Int) = apiService.getVideos(movieId.toString(), BuildConfig.API_KEY)
}