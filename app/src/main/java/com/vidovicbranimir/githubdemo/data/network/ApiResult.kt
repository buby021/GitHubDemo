package com.vidovicbranimir.githubdemo.data.network

import okhttp3.ResponseBody

sealed class ApiResult<out T> {
    data class Success<out T>(val value:T) : ApiResult<T>()
    data class Failure(
        val isNetworkError : Boolean,
        val errorCode: Int?,
        val errorBofy: ResponseBody?
    ) : ApiResult<Nothing>()
}