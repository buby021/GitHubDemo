package com.vidovicbranimir.githubdemo.data.repository

import com.vidovicbranimir.githubdemo.data.network.ApiService

class MainRepository(private val api: ApiService): BaseRepository() {

    suspend fun getUser() = safeApiCall { api.getUser() }
}