package com.example.githubexplorer.ui.userdetail

sealed interface UserDetailUiState {
    data object Loading : UserDetailUiState
    data class Content(
        val userDetailDisplayData: UserDetailDisplayData,
    ) : UserDetailUiState

    data class Error(val message: String) : UserDetailUiState
}
