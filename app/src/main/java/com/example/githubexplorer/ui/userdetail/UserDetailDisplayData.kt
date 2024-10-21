package com.example.githubexplorer.ui.userdetail

import androidx.annotation.DrawableRes
import com.example.githubexplorer.R

data class UserDetailDisplayData(
    val imageUrl: String,
    val username: String,
    val fullName: String,
    val formattedFollowing: String,
    val formattedFollowers: String,
    val bio: String?,
    val additionalData: List<AdditionalData>,
)

data class AdditionalData(
    val type: AdditionalDataType,
    val value: String,
)

enum class AdditionalDataType(@DrawableRes val resourceId: Int) {
    TWITTER(R.drawable.ic_twitter),
    COMPANY(R.drawable.ic_company),
    LOCATION(R.drawable.ic_location),
    EMAIL(R.drawable.ic_email),
}
