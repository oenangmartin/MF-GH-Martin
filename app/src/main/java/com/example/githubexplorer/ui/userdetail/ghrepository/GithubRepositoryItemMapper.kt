package com.example.githubexplorer.ui.userdetail.ghrepository

import com.example.githubexplorer.common.formatter.NumberFormatter
import com.example.network.model.GithubRepositoryItem

class GithubRepositoryItemMapper(
    private val numberFormatter: NumberFormatter,
) {
    fun map(item: GithubRepositoryItem): GithubRepositoryItemDisplayData {
        return GithubRepositoryItemDisplayData(
            id = item.id,
            repositoryName = item.name,
            repositoryFullName = item.fullName,
            developmentLanguage = item.language,
            formattedNumberOfStars = numberFormatter.getAbbreviatedNumberFormat(item.numberOfStar),
            formattedNumberOfForks = numberFormatter.getAbbreviatedNumberFormat(item.numberOfForks),
            description = item.description,
            url = item.htmlUrl,
        )
    }
}