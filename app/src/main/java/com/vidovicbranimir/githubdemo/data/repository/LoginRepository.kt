package com.vidovicbranimir.githubdemo.data.repository

import com.vidovicbranimir.githubdemo.data.UserPreferences
import com.vidovicbranimir.githubdemo.data.model.AccessToken
import com.vidovicbranimir.githubdemo.data.network.ApiService

class LoginRepository(private val api: ApiService, private val preferences: UserPreferences) :
    BaseRepository() {

    private val clientId = "07f1a1a2948e882fa91f"
    private val clientSecret = "0ba762306e0f0e15bbd7f7c99df817c1ef9d4773"
    private val redirectUri = "githubdemo://callback"

    suspend fun getAuthToken(code: String) = safeApiCall { api.login(clientId, clientSecret, code) }

    suspend fun saveAuthToken(accessToken: AccessToken) {
        preferences.saveAuthToken(accessToken.accessToken)
    }

}