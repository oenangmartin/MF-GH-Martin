package com.example.githubexplorer.ui.userdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubexplorer.common.ui.FullScreenLoading
import com.example.githubexplorer.ui.theme.GithubExplorerTheme
import com.example.githubexplorer.ui.userdetail.ghrepository.GithubRepositoryScreens
import com.example.githubexplorer.ui.userdetail.navigaton.UserDetailRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserDetailRoute(
    route: UserDetailRoute
) {
    val viewModel = koinViewModel<UserDetailViewModel>(
        parameters = {
            parametersOf(route.username)
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    UserDetailScreen(
        modifier = Modifier,
        uiState = uiState,
        username = route.username
    )
}

@Composable
fun UserDetailScreen(
    modifier: Modifier = Modifier,
    uiState: UserDetailUiState,
    username: String,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        when (uiState) {
            is UserDetailUiState.Content -> {
                Column {
                    UserInfoSection(
                        modifier = Modifier,
                        userDetailDisplayData = uiState.userDetailDisplayData
                    )
                    GithubRepositoryScreens(
                        modifier = Modifier,
                        username = username,
                    )
                }
            }

            is UserDetailUiState.Error -> {
                Text(text = uiState.message)
            }

            UserDetailUiState.Loading -> {
                FullScreenLoading()
            }
        }
    }
}

@Preview
@Composable
private fun UserDetailScreenPreview() {
    GithubExplorerTheme {
//        UserDetailScreen(viewModel = koinViewModel())

    }
}