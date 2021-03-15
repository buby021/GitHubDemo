package com.vidovicbranimir.githubdemo.data.network.responses

import java.io.Serializable

data class License(
    val html_url: String,
    val key: String,
    val name: String,
    val node_id: String,
    val spdx_id: String,
    val url: String
) :Serializable