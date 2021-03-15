package com.vidovicbranimir.githubdemo.data.network.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GitRepositoryResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repos: List<Repo>,
    @SerializedName("total_count")
    val totalCount: Int
): Serializable