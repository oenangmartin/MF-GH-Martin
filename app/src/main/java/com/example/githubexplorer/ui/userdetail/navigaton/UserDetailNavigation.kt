package com.example.githubexplorer.ui.userdetail.navigaton

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.example.githubexplorer.ui.userdetail.UserDetailRoute as UserDetailRouteComp

@Serializable
data class UserDetailRoute(val username: String)

fun NavController.navigateToUserDetail(username: String) {
    navigate(UserDetailRoute(username))
}

fun NavGraphBuilder.userDetailScreen() {
    composable<UserDetailRoute> {
        val args = it.toRoute<UserDetailRoute>()
        UserDetailRouteComp(args)
    }
}
