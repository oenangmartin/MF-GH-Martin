package com.example.githubexplorer.repository.helper

class DefaultPaginator<Key, Query, Item>(
    private val initialKey: Key,
    private inline val onLoading: (Boolean) -> Unit,
    private inline val onReset: () -> Unit,
    private inline val onLoadMore: suspend (nextKey: Key, query: Query) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onSuccess: suspend (items: List<Item>) -> Unit,
    private inline val onError: suspend (Throwable) -> Unit,
) : Paginator<Key, Query, Item> {
    private var currentKey: Key = initialKey
    private var isLoading: Boolean = false

    override suspend fun loadNextItems(query: Query) {
        if (isLoading) return
        isLoading = true
        onLoading(true)
        onLoadMore(currentKey, query)
            .onSuccess {
                isLoading = false
                onLoading(false)
                onSuccess(it)
                currentKey = getNextKey(it)
            }
            .onFailure {
                isLoading = false
                onLoading(false)
                onError(it)
            }
    }

    override fun reset() {
        currentKey = initialKey
        onReset()
    }

}