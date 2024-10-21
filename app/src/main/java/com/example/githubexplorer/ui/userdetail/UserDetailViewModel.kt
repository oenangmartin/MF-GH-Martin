package com.example.githubexplorer.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.coroutine.launchOnMain
import com.example.githubexplorer.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserDetailViewModel(
    private val username: String,
    private val userRepository: UserRepository,
    private val userDetailMapper: UserDetailMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserDetailUiState>(UserDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchInitialData()
    }

    private fun updateUiState(newUiState: UserDetailUiState) {
        _uiState.update { newUiState }
    }

    private fun fetchInitialData() {
        viewModelScope.launchOnMain(dispatcherProvider) {
            userRepository.getUserDetail(username)
                .onSuccess {
                    updateUiState(
                        UserDetailUiState.Content(
                            userDetailDisplayData = userDetailMapper.map(it),
                        )
                    )
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }
}