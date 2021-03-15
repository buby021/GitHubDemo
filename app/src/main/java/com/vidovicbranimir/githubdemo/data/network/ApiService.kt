package com.vidovicbranimir.githubdemo.data.network

import com.vidovicbranimir.githubdemo.data.network.responses.GitRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    suspend fun login(): Any

    @GET("search/repositories")
    suspend fun getRepositrories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): GitRepositoryResponse
}