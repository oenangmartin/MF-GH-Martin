package com.example.githubexplorer.repository

import com.example.network.model.GithubRepositoryItem

interface GithubRepoRepository {
    suspend fun getNonForkedGithubRepositoryList(
        username: String,
        page: Int,
        pageSize: Int
    ): Result<List<GithubRepositoryItem>>
}