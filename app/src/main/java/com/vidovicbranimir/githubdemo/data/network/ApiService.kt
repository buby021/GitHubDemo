package com.vidovicbranimir.githubdemo.data.network

import com.vidovicbranimir.githubdemo.data.model.AccessToken
import com.vidovicbranimir.githubdemo.data.network.responses.GitRepositoryResponse
import com.vidovicbranimir.githubdemo.data.network.responses.User
import retrofit2.http.*

interface ApiService {

    @Headers("Accept: application/json")
    @POST
    @FormUrlEncoded
    suspend fun login(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Url url: String = "https://github.com/login/oauth/access_token"
    ): AccessToken

    // couldnt find proper not deprecated endpoint for logout
    @DELETE
    suspend fun logout(@Url url: String = "https://github.com/logout")

//    @DELETE("installation/token")
//    suspend fun logout()

    @GET("search/repositories")
    suspend fun getRepositrories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): GitRepositoryResponse

    @GET("user")
    suspend fun getUser(): User
}