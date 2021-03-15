package com.vidovicbranimir.githubdemo.ui.base

import androidx.lifecycle.ViewModel
import com.vidovicbranimir.githubdemo.data.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {

}