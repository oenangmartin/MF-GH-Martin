package com.example.githubexplorer.ui.userlist

data class UserListUiState(
    val isLoading: Boolean = false,
    val panelState: PanelState = PanelState.Loading
) {
    sealed interface PanelState {
        data object Loading : PanelState
        data class Content(
            val userList: List<UserItemDisplayData>
        ) : PanelState

        data class Error(val message: String) : PanelState
    }
}
