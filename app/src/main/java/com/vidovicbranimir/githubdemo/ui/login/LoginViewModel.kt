package com.vidovicbranimir.githubdemo.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vidovicbranimir.githubdemo.data.model.AccessToken
import com.vidovicbranimir.githubdemo.data.network.ApiResult
import com.vidovicbranimir.githubdemo.data.repository.LoginRepository
import com.vidovicbranimir.githubdemo.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository
) : BaseViewModel(loginRepository) {

    private val _loginResponse: MutableLiveData<ApiResult<AccessToken>> = MutableLiveData()
    val loginResponse: LiveData<ApiResult<AccessToken>>
        get() =_loginResponse


    fun getAuthToken(code: String) {
        viewModelScope.launch {
            _loginResponse.value = ApiResult.Loading
           _loginResponse.value = loginRepository.getAuthToken(code)

        }
    }

    suspend fun saveAuthToken(token: AccessToken) {
        loginRepository.saveAuthToken(token)
    }
}