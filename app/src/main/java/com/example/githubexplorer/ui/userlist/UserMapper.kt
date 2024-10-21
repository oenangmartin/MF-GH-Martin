package com.example.githubexplorer.ui.userlist

import com.example.network.model.User

class UserMapper {
    fun map(user: User): UserItemDisplayData {
        return UserItemDisplayData(
            id = user.id,
            displayName = user.login,
            imageUrl = user.avatarUrl,
        )
    }
}