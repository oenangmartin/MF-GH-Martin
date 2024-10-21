package com.example.githubexplorer.ui.userlist.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.githubexplorer.ui.userlist.UserListRoute
import kotlinx.serialization.Serializable

@Serializable
object UserListRoute

fun NavGraphBuilder.userListScreen(
    onUserClicked: (String) -> Unit,
) {
    composable<UserListRoute> {
        UserListRoute(onUserClicked)
    }
}