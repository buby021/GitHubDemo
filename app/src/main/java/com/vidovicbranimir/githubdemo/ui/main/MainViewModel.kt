package com.vidovicbranimir.githubdemo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vidovicbranimir.githubdemo.data.network.ApiResult
import com.vidovicbranimir.githubdemo.data.network.responses.User
import com.vidovicbranimir.githubdemo.data.repository.MainRepository
import com.vidovicbranimir.githubdemo.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository) : BaseViewModel(mainRepository) {

    private val _user: MutableLiveData<ApiResult<User>> = MutableLiveData()
    val user: LiveData<ApiResult<User>>
        get() = _user

    fun getUser() {
        viewModelScope.launch {
            _user.value = ApiResult.Loading
            _user.value = mainRepository.getUser()
        }
    }

}