package com.example.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchUserResponse(
    @Json(name = "total_count")
    val totalCount: Long,
    @Json(name = "incomplete_results")
    val incompleteResult: Boolean,
    @Json(name = "items")
    val items: List<User>,
)