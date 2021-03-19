package com.vidovicbranimir.githubdemo.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {
    companion object {
        private const val BASE_URL = "https://api.github.com/"
    }

    fun buildApi(
        authToken: String? = null
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        chain.proceed(chain.request().newBuilder().also {
                            it.addHeader("Authorization", "Bearer $authToken")
                        }.build())
                    }.build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    val clearApiService: ApiService? = null
}