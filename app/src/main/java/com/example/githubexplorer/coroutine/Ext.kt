package com.example.githubexplorer.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.launchOnMain(
    dispatcherProvider: DispatcherProvider,
    block: suspend CoroutineScope.() -> Unit,
) {
    launch(dispatcherProvider.main(), block = block)
}