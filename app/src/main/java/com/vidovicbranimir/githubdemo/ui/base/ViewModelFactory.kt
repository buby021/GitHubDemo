package com.vidovicbranimir.githubdemo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vidovicbranimir.githubdemo.data.repository.BaseRepository
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.data.repository.LoginRepository
import com.vidovicbranimir.githubdemo.data.repository.MainRepository
import com.vidovicbranimir.githubdemo.ui.login.LoginViewModel
import com.vidovicbranimir.githubdemo.ui.main.MainViewModel
import com.vidovicbranimir.githubdemo.ui.repository.RepositoryViewModel

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RepositoryViewModel::class.java) -> RepositoryViewModel(
                repository as GitRepositoryRepository
            ) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(
                repository as LoginRepository
            ) as T
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                repository as MainRepository
            ) as T
            else -> throw IllegalArgumentException("ViewModel class not found")
        }
    }

}