package com.example.githubexplorer.ui.userdetail.ghrepository

import com.example.githubexplorer.common.formatter.NumberFormatter
import com.example.network.model.GithubRepositoryItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GithubRepositoryItemMapperTest : DescribeSpec({
    val numberFormatter: NumberFormatter = mockk()
    lateinit var mapper: GithubRepositoryItemMapper
    beforeEach {
        mapper = GithubRepositoryItemMapper(numberFormatter)
    }

    describe("map") {
        context("when triggered") {
            it("should return data correctly") {
                val item = GithubRepositoryItem(
                    id = "789",
                    name = "Repository 3",
                    fullName = "owner/Repository 3",
                    htmlUrl = "https://github.com/owner/Repository3",
                    fork = false,
                    numberOfStar = 200,
                    numberOfForks = 30,
                    description = "This is the third repository.",
                    language = "Python"
                )
                every { numberFormatter.getAbbreviatedNumberFormat(200) } returns "200"
                every { numberFormatter.getAbbreviatedNumberFormat(30) } returns "30"
                val result = mapper.map(item)
                result shouldBe GithubRepositoryItemDisplayData(
                    id = "789",
                    repositoryName = "Repository 3",
                    repositoryFullName = "owner/Repository 3",
                    developmentLanguage = "Python",
                    formattedNumberOfStars = "200",
                    formattedNumberOfForks = "30",
                    description = "This is the third repository.",
                    url = "https://github.com/owner/Repository3",
                )
            }
        }
    }
})