package com.example.githubexplorer.ui.userdetail.ghrepository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubexplorer.LocalSnackbarHostState
import com.example.githubexplorer.common.ui.FullScreenLoading
import com.example.githubexplorer.common.ui.LoadMoreLazyColumn
import com.example.githubexplorer.common.ui.launchChromeTab
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun GithubRepositoryScreens(
    modifier: Modifier = Modifier,
    username: String,
) {
    val viewModel = koinViewModel<GithubRepositoryListViewModel>(
        parameters = {
            parametersOf(username)
        }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState by viewModel.snackbarState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(snackbarState.message) {
        if (snackbarState.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarState.message)
            viewModel.setSnackbarMessageShown()
        }
    }
    val context = LocalContext.current
    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {
        when (uiState) {
            is GithubRepositoryListUiState.Content -> {
                GithubRepositoryListContent(
                    modifier = Modifier,
                    uiState = uiState as GithubRepositoryListUiState.Content,
                    onItemClick = {
                        context.launchChromeTab(it.url)
                    },
                    onLoadMore = viewModel::loadMore
                )
            }

            is GithubRepositoryListUiState.Error -> {
                Text((uiState as GithubRepositoryListUiState.Error).message)
            }

            GithubRepositoryListUiState.Loading -> {
                FullScreenLoading()
            }
        }
    }
}

@Composable
fun GithubRepositoryListContent(
    modifier: Modifier = Modifier,
    uiState: GithubRepositoryListUiState.Content,
    onItemClick: (GithubRepositoryItemDisplayData) -> Unit,
    onLoadMore: () -> Unit,
) {
    LoadMoreLazyColumn(
        modifier = modifier,
        isLoading = uiState.isLoading,
        onLoadMore = { onLoadMore() },
    ) {
        items(uiState.repositories, key = { it.id }) {
            GithubRepositoryItemContent(
                modifier = Modifier,
                item = it,
                onItemClick = onItemClick
            )
        }
    }
}