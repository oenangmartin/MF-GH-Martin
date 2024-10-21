package com.example.githubexplorer.ui.userdetail.ghrepository

data class GithubRepositoryItemDisplayData(
    val id: String,
    val repositoryName: String,
    val repositoryFullName: String,
    val developmentLanguage: String?,
    val formattedNumberOfStars: String,
    val formattedNumberOfForks: String,
    val description: String?,
    val url: String,
)