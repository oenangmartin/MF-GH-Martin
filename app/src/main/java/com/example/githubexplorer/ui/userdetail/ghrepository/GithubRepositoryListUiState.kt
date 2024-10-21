package com.example.githubexplorer.ui.userdetail.ghrepository

sealed interface GithubRepositoryListUiState {
    data object Loading : GithubRepositoryListUiState
    data class Content(
        val isLoading: Boolean,
        val repositories: List<GithubRepositoryItemDisplayData>,
    ) : GithubRepositoryListUiState

    data class Error(val message: String) : GithubRepositoryListUiState
}
