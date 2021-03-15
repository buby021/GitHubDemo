package com.vidovicbranimir.githubdemo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vidovicbranimir.githubdemo.data.repository.BaseRepository
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.ui.repository.RepositoryViewModel

class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RepositoryViewModel::class.java) -> RepositoryViewModel(
                repository as GitRepositoryRepository
            ) as T
            else -> throw IllegalArgumentException("ViewModel class not found")
        }
    }

}