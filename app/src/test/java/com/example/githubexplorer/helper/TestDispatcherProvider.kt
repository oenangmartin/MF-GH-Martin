package com.example.githubexplorer.helper

import com.example.githubexplorer.coroutine.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider: DispatcherProvider {
    override fun main(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun io(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun default(): CoroutineDispatcher = Dispatchers.Unconfined
}