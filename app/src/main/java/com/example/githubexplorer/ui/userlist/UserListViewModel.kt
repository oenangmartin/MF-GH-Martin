package com.example.githubexplorer.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubexplorer.common.SnackbarState
import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.coroutine.launchOnMain
import com.example.githubexplorer.repository.UserRepository
import com.example.githubexplorer.repository.helper.DefaultPaginator
import com.example.network.exception.CommonError
import com.example.network.exception.NoInternetException
import com.example.network.model.User
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

private const val PAGE_SIZE = 20

class UserListViewModel(
    private val userListRepository: UserRepository,
    private val userMapper: UserMapper,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {
    private val currentList = mutableListOf<User>()
    private val searchQuery = MutableStateFlow("")
    private val _uiState: MutableStateFlow<UserListUiState> = MutableStateFlow(UserListUiState())
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()
    private val _snackbarState: MutableStateFlow<SnackbarState> =
        MutableStateFlow(SnackbarState(""))
    val snackbarState: StateFlow<SnackbarState> = _snackbarState.asStateFlow()
    private var hasReachedEnd = false
    private var currentPage = 0L

    init {
        initialize()
    }

    private val paginator = DefaultPaginator(
        initialKey = 0L,
        onLoading = { onLoadingUpdated(it) },
        onReset = { currentList.clear() },
        onLoadMore = { key, query: String -> onLoadMore(query, key) },
        getNextKey = { getNextKey() },
        onSuccess = { onLoadItemSuccess(it) },
        onError = { handleOnError(it) }
    )

    private fun onLoadingUpdated(isLoading: Boolean) {
        updateUiState(
            _uiState.value.copy(
                isLoading = isLoading,
                panelState = _uiState.value.panelState
            )
        )
    }

    private suspend fun onLoadMore(
        query: String,
        key: Long,
    ): Result<List<User>> {
        return if (query.isEmpty()) {
            userListRepository.getUserList(key, PAGE_SIZE)
        } else {
            userListRepository.getUserListWithQuery(query, key, PAGE_SIZE)
        }
    }

    private fun getNextKey(): Long {
        return if (searchQuery.value.isEmpty()) {
            currentList.lastOrNull()?.id ?: 0L
        } else {
            currentPage++
            currentPage
        }
    }

    private fun onLoadItemSuccess(items: List<User>) {
        if (items.size < PAGE_SIZE) hasReachedEnd = true
        if (items.isEmpty() && currentList.isEmpty()) {
            updateUiState(
                UserListUiState(
                    isLoading = false,
                    panelState = UserListUiState.PanelState.Error("No user found")
                )
            )
        } else {
            currentList.addAll(items)
            updateUiState(
                _uiState.value.copy(
                    isLoading = false,
                    panelState = UserListUiState.PanelState.Content(
                        userList = currentList.map { userMapper.map(it) }
                    )
                )
            )
        }
    }

    private fun handleOnError(throwable: Throwable?) {
        val errorMessage = when (throwable) {
            is NoInternetException -> "No internet connection"
            is CommonError -> throwable.message + "Error Code (${throwable.resultCode})"
            else -> "Unknown Error"
        }

        if (currentList.isEmpty()) {
            updateUiState(
                _uiState.value.copy(
                    isLoading = false,
                    panelState = UserListUiState.PanelState.Error(errorMessage)
                )
            )
        } else {
            _snackbarState.update {
                SnackbarState(errorMessage)
            }
        }
    }

    private fun updateUiState(newUiState: UserListUiState) {
        _uiState.update { newUiState }
    }

    @OptIn(FlowPreview::class)
    private fun initialize() {
        searchQuery.debounce(500)
            .onEach {
                paginator.reset()
                paginator.loadNextItems(it)
            }
            .launchIn(viewModelScope)
        viewModelScope.launchOnMain(dispatcherProvider) {
            searchQuery.emit("")
        }
    }

    fun onSearchQueryChanged(query: String) {
        viewModelScope.launchOnMain(dispatcherProvider) {
            searchQuery.emit(query)
        }
    }

    fun loadMore(query: String) {
        if (hasReachedEnd) return
        viewModelScope.launchOnMain(dispatcherProvider) {
            paginator.loadNextItems(query)
        }
    }

    fun setSnackbarMessageShown() {
        _snackbarState.update {
            SnackbarState("")
        }
    }
}