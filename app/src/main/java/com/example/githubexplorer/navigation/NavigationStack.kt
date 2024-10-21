package com.example.githubexplorer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.githubexplorer.ui.userdetail.navigaton.navigateToUserDetail
import com.example.githubexplorer.ui.userdetail.navigaton.userDetailScreen
import com.example.githubexplorer.ui.userlist.navigation.UserListRoute
import com.example.githubexplorer.ui.userlist.navigation.userListScreen

@Composable
fun NavigationStack(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = UserListRoute
    ) {
        userListScreen { username ->
            navController.navigateToUserDetail(username = username)
        }
        userDetailScreen()
    }
}