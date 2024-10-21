package com.example.githubexplorer.repository.helper

interface Paginator<Key, Query, Item> {
    suspend fun loadNextItems(query: Query)
    fun reset()
}