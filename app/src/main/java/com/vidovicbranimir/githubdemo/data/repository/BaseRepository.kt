package com.vidovicbranimir.githubdemo.data.repository

import com.vidovicbranimir.githubdemo.data.network.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safaApiCall(
        apiCall : suspend () -> T
    ) : ApiResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                ApiResult.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        ApiResult.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else ->
                        ApiResult.Failure(true, null, null)
                }
            }
        }
    }

}