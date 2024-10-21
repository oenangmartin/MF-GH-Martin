package com.example.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GithubRepositoryItem(
    val id: String,
    val name: String,
    @Json(name ="full_name")
    val fullName: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    val fork: Boolean,
    @Json(name = "stargazers_count")
    val numberOfStar: Long,
    @Json(name = "forks_count")
    val numberOfForks: Long,
    val description: String?,
    val language: String?,
)