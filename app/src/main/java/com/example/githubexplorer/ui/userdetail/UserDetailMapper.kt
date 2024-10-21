package com.example.githubexplorer.ui.userdetail

import com.example.githubexplorer.common.formatter.NumberFormatter
import com.example.network.model.UserDetail

class UserDetailMapper(
    private val numberFormatter: NumberFormatter,
) {
    fun map(userDetail: UserDetail): UserDetailDisplayData {
        val additionalData = mutableListOf<AdditionalData>()
        userDetail.company?.let {
            getAdditionalData(
                it,
                AdditionalDataType.COMPANY
            )?.let { data ->
                additionalData.add(data)
            }
        }

        userDetail.location?.let {
            getAdditionalData(
                it,
                AdditionalDataType.LOCATION
            )?.let { data ->
                additionalData.add(data)
            }
        }

        userDetail.email?.let {
            getAdditionalData(
                it,
                AdditionalDataType.EMAIL
            )?.let { data ->
                additionalData.add(data)
            }
        }

        userDetail.twitterUsername?.let {
            getAdditionalData(
                it,
                AdditionalDataType.TWITTER
            )?.let { data ->
                additionalData.add(data)
            }
        }

        return UserDetailDisplayData(
            imageUrl = userDetail.avatarUrl,
            username = userDetail.username,
            fullName = userDetail.name ?: userDetail.username,
            formattedFollowing = "Following: ${numberFormatter.getAbbreviatedNumberFormat(userDetail.following)}",
            formattedFollowers = "Followers: ${numberFormatter.getAbbreviatedNumberFormat(userDetail.followers)}",
            additionalData = additionalData.toList(),
            bio = userDetail.bio,
        )
    }

    // todo this can be moved to another mapper
    private fun getAdditionalData(value: String, type: AdditionalDataType): AdditionalData? {
        if (value.isEmpty()) return null
        return AdditionalData(
            value = value,
            type = type,
        )
    }
}