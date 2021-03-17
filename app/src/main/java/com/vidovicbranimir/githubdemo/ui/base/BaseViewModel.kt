package com.vidovicbranimir.githubdemo.ui.base

import androidx.lifecycle.ViewModel
import com.vidovicbranimir.githubdemo.data.network.ApiService
import com.vidovicbranimir.githubdemo.data.repository.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

    suspend fun logout(api: ApiService) = withContext(Dispatchers.IO) { repository.logout(api) }

}