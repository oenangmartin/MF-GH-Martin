package com.example.githubexplorer.ui.userdetail.ghrepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.common.SnackbarState
import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.coroutine.launchOnMain
import com.example.githubexplorer.repository.GithubRepoRepository
import com.example.githubexplorer.repository.helper.DefaultPaginator
import com.example.network.exception.CommonError
import com.example.network.exception.NoInternetException
import com.example.network.model.GithubRepositoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


private const val DEFAULT_PAGE_SIZE = 20

class GithubRepositoryListViewModel(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val username: String,
    private val repository: GithubRepoRepository,
    private val itemMapper: GithubRepositoryItemMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val _uiState: MutableStateFlow<GithubRepositoryListUiState> =
        MutableStateFlow(GithubRepositoryListUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private val _snackbarState: MutableStateFlow<SnackbarState> =
        MutableStateFlow(SnackbarState(""))
    val snackbarState: StateFlow<SnackbarState> = _snackbarState.asStateFlow()
    private var hasReachedEnd = false

    private val currentList = mutableListOf<GithubRepositoryItem>()
    private val paginator = DefaultPaginator(
        initialKey = 1,
        onLoading = { onLoadingUpdated(it) },
        onReset = { currentList.clear() },
        onLoadMore = { key, query: String -> onLoadMore(query, key) },
        getNextKey = { currentList.size / pageSize + 1 },
        onSuccess = { onLoadItemSuccess(it) },
        onError = { handleOnError(it) }
    )

    init {
        viewModelScope.launchOnMain(dispatcherProvider) { paginator.loadNextItems(username) }
    }

    private fun onLoadingUpdated(isLoading: Boolean) {
        if (_uiState.value is GithubRepositoryListUiState.Content) {
            updateUiState(
                (_uiState.value as GithubRepositoryListUiState.Content).copy(
                    isLoading = isLoading
                )
            )
        }
    }

    private fun handleOnError(throwable: Throwable) {
        val errorMessage = when (throwable) {
            is NoInternetException -> "No internet connection"
            is CommonError -> throwable.message + "Error Code (${throwable.resultCode})"
            else -> "Unknown Error"
        }
        if (currentList.isEmpty()) {
            updateUiState(GithubRepositoryListUiState.Error(errorMessage))
        } else {
            _snackbarState.update { SnackbarState(errorMessage) }
        }

    }

    private fun onLoadItemSuccess(it: List<GithubRepositoryItem>) {
        // don't load more when the fetched size is less than page size
        if (it.size < pageSize) hasReachedEnd = true

        if (it.isEmpty() && currentList.isEmpty()) {
            updateUiState(GithubRepositoryListUiState.Error("No repositories from this user"))
        } else {
            currentList.addAll(it)
            updateUiState(
                GithubRepositoryListUiState.Content(
                    isLoading = false,
                    repositories = currentList.map { item -> itemMapper.map(item) }
                )
            )
        }
    }

    private suspend fun onLoadMore(query: String, key: Int): Result<List<GithubRepositoryItem>> {
        return repository.getNonForkedGithubRepositoryList(
            username = username,
            page = key,
            pageSize = pageSize,
        )
    }

    private fun updateUiState(newUiState: GithubRepositoryListUiState) {
        _uiState.update { newUiState }
    }

    fun loadMore() {
        if (hasReachedEnd) return
        viewModelScope.launchOnMain(dispatcherProvider) {
            paginator.loadNextItems(username)
        }
    }

    fun setSnackbarMessageShown() {
        _snackbarState.update {
            SnackbarState("")
        }
    }
}