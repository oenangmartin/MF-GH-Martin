package com.example.githubexplorer.repository

import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.network.GithubApiService
import com.example.network.helper.safeApiCall
import com.example.network.model.GithubRepositoryItem

class GithubRepoRepositoryImpl(
    private val githubApiService: GithubApiService,
    private val dispatcherProvider: DispatcherProvider,
) : GithubRepoRepository {
    override suspend fun getNonForkedGithubRepositoryList(
        username: String,
        page: Int,
        pageSize: Int,
    ): Result<List<GithubRepositoryItem>> {
        return safeApiCall(dispatcherProvider.io()) {
            githubApiService.getGithubRepositoryList(username, page, perPage = pageSize)
                .filter { it.fork.not() }
        }
    }
}