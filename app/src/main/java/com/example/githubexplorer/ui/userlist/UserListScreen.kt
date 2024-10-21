package com.example.githubexplorer.ui.userlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.githubexplorer.LocalSnackbarHostState
import com.example.githubexplorer.common.ui.FullScreenLoading
import com.example.githubexplorer.common.ui.LoadMoreLazyColumn
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun UserListRoute(
    onUserClicked: (String) -> Unit,
) {
    val viewModel = koinViewModel<UserListViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarState by viewModel.snackbarState.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(snackbarState.message) {
        if (snackbarState.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(snackbarState.message)
            viewModel.setSnackbarMessageShown()
        }
    }

    UserListScreen(
        modifier = Modifier,
        panelState = uiState.panelState,
        onUserClicked = onUserClicked,
        isLoading = uiState.isLoading,
        onLoadMore = { viewModel.loadMore(it) },
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) }
    )
}

@Composable
internal fun UserListScreen(
    modifier: Modifier = Modifier,
    panelState: UserListUiState.PanelState,
    isLoading: Boolean,
    onUserClicked: (String) -> Unit,
    onLoadMore: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        when (panelState) {
            is UserListUiState.PanelState.Loading -> {
                FullScreenLoading()
            }

            is UserListUiState.PanelState.Content -> {
                UserListContent(
                    userList = panelState.userList,
                    onLoadMore = onLoadMore,
                    isLoading = isLoading,
                    onUserClicked = onUserClicked,
                    onSearchQueryChanged = onSearchQueryChanged,
                )
            }

            is UserListUiState.PanelState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = panelState.message)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListContent(
    modifier: Modifier = Modifier,
    userList: List<UserItemDisplayData>,
    isLoading: Boolean,
    onLoadMore: (String) -> Unit,
    onUserClicked: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
) {
    var searchedText by rememberSaveable { mutableStateOf("") }
    LoadMoreLazyColumn(
        modifier = modifier,
        isLoading = isLoading,
        topContent = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchedText,
                        onQueryChange = {
                            searchedText = it
                            onSearchQueryChanged.invoke(it)
                        },
                        onSearch = { },
                        expanded = false,
                        onExpandedChange = { },
                        placeholder = { Text("Search by Username") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = { },
                    )
                },
                expanded = false,
                onExpandedChange = { },
                content = { }
            )
        },
        onLoadMore = { onLoadMore(searchedText) },
    ) {
        items(userList, key = { it.id }) { item ->
            UserItem(user = item, onClick = onUserClicked)
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: UserItemDisplayData,
    onClick: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        onClick = {
            onClick(user.displayName)
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                model = user.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = user.displayName)
        }
    }
}