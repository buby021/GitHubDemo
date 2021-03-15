package com.vidovicbranimir.githubdemo.ui.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vidovicbranimir.githubdemo.data.network.ApiService
import com.vidovicbranimir.githubdemo.data.network.responses.Repo
import retrofit2.HttpException
import java.io.IOException


private const val GITHUB_STARTING_PAGE_INDEX = 1
private const val PER_PAGE = 50
private const val MAX_NETWORK_LOAD_ITEMS = 1000

class GithubPagingSource(
    private val api: ApiService,
    private val query: String,
    private val sort: String,
    private val order: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            val response = api.getRepositrories(query, sort, order, PER_PAGE, position)
            val repos = response.repos
            val nextPageNumber = params?.key ?: 0
            LoadResult.Page(
                data = repos,
                prevKey = if (nextPageNumber > 0) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < MAX_NETWORK_LOAD_ITEMS / PER_PAGE) nextPageNumber + 1 else null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }

    }

}