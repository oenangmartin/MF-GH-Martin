package com.example.githubexplorer.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun LoadMoreLazyColumn(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    topContent: (@Composable () -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val listState = rememberLazyListState()
        val reachedBottom: Boolean by remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
            }
        }

        LaunchedEffect(reachedBottom) {
            if (reachedBottom) {
                onLoadMore.invoke()
            }
        }

        topContent?.invoke()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
            if (isLoading) {
                item {
                    CircularProgressIndicator()
                }
            }
        }
    }
}