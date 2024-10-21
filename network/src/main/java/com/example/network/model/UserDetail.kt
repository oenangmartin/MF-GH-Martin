package com.example.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDetail(
    @Json(name = "login")
    val username: String,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    val name: String?,
    val followers: Long,
    val following: Long,
    @Json(name = "public_repos")
    val publicRepos: Long,
    val email: String?,
    val location: String?,
    val company: String?,
    val bio: String?,
    @Json(name = "twitter_username")
    val twitterUsername: String?,
)