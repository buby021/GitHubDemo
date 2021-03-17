package com.vidovicbranimir.githubdemo.data.network.responses

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)