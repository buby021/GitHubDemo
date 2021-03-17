package com.vidovicbranimir.githubdemo.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {
    companion object {
        private const val BASE_URL = "https://api.github.com/"
//        private const val BASE_URL = "https://github.com/"
//        private const val authToken = "913ac5efe4e9d3a2c00b80dafadd7b3eae1c7d3d"

    }
//
//    val buildApi: ApiService by lazy {
//        return@lazy Retrofit.Builder()
//            .baseUrl(BASE_URL)a
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor { chain ->
//                        chain.proceed(chain.request().newBuilder().also {
//                            it.addHeader("Authorization", "Bearer $authToken")
//                        }.build())
//                    }.build()
//            ).addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(ApiService::class.java)
//    }

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