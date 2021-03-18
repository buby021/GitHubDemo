package com.vidovicbranimir.githubdemo.data.repository

import com.vidovicbranimir.githubdemo.data.UserPreferences
import com.vidovicbranimir.githubdemo.data.model.AccessToken
import com.vidovicbranimir.githubdemo.data.network.ApiService
import com.vidovicbranimir.githubdemo.utils.Constants

class LoginRepository(private val api: ApiService, private val preferences: UserPreferences) :
    BaseRepository() {

    suspend fun getAuthToken(code: String) = safeApiCall { api.login(Constants.CLIENT_ID, Constants.CLIENT_SECRET, code) }

    suspend fun saveAuthToken(accessToken: AccessToken) {
        preferences.saveAuthToken(accessToken.accessToken)
    }

}