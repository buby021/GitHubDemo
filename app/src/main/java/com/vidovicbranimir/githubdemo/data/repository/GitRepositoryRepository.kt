package com.vidovicbranimir.githubdemo.data.repository

import androidx.paging.*
import com.vidovicbranimir.githubdemo.data.UserPreferences
import com.vidovicbranimir.githubdemo.data.network.ApiService
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import com.vidovicbranimir.githubdemo.ui.repository.GithubPagingSource
import kotlinx.coroutines.flow.Flow


class GitRepositoryRepository(private val api: ApiService, private val preferences: UserPreferences) : BaseRepository() {

    fun getSearchResultStream(query: String, sort: String, order: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(api, query, sort, order) }
        ).flow
    }



}