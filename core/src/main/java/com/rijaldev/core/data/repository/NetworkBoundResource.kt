package com.rijaldev.core.data.repository

import com.rijaldev.core.data.source.remote.retrofit.ApiResponse
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> Flow<ApiResponse<RequestType>>,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true }
) = flow {
    emit(Resource.Loading())
    val data = query().first()
    if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        when (val response = fetch().first()) {
            is ApiResponse.Success -> {
                saveFetchResult(response.data)
                emitAll(query().map { Resource.Success(it) })
            }
            is ApiResponse.Empty -> emitAll(query().map { Resource.Success(it) })
            is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
        }
    } else {
        emitAll(query().map { Resource.Success(it) })
    }
}