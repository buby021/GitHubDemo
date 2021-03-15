package com.vidovicbranimir.githubdemo.ui.repository

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.data.repository.GitRepositoryRepository
import com.vidovicbranimir.githubdemo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class RepositoryViewModel(
    private val gitRepositoryRepository: GitRepositoryRepository
) : BaseViewModel(gitRepositoryRepository) {

    private var currentQueryValue: String? = null
    private var currentSortValue: String? = null
    private var currentOrderValue: String? = null

    private var currentSearchResult: Flow<PagingData<Repo>>? = null

    fun searchRepo(queryString: String, sort: String, order: String): Flow<PagingData<Repo>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && sort == currentSortValue && order == currentOrderValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        currentSortValue = sort
        currentOrderValue = order
        val newResult: Flow<PagingData<Repo>> = gitRepositoryRepository.getSearchResultStream(queryString, sort, order)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}