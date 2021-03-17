package com.vidovicbranimir.githubdemo.data.network

import okhttp3.ResponseBody

sealed class ApiResult<out T> {
    data class Success<out T>(val value:T) : ApiResult<T>()
    data class Failure(
        val isNetworkError : Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}