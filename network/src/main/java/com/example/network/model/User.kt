package com.example.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: Long,
    @Json(name = "avatar_url")
    val avatarUrl: String,
    val login: String,
)