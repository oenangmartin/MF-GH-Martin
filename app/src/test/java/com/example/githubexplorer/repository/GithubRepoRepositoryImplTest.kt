package com.example.githubexplorer.repository

import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.helper.TestDispatcherProvider
import com.example.network.GithubApiService
import com.example.network.model.GithubRepositoryItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class GithubRepoRepositoryImplTest : DescribeSpec({

    val githubApiService: GithubApiService = mockk()
    val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()
    lateinit var repositoryImpl: GithubRepoRepositoryImpl

    beforeEach {
        repositoryImpl = GithubRepoRepositoryImpl(githubApiService, dispatcherProvider)
    }

    describe("getNonForkedGithubRepositoryList") {
        context("when triggered and result success") {
            it("should return only non-forked repository") {
                val githubRepositoryItems = listOf(
                    GithubRepositoryItem(
                        id = "123",
                        name = "Repository 1",
                        fullName = "owner/Repository 1",
                        htmlUrl = "https://github.com/owner/Repository1",
                        fork = false,
                        numberOfStar = 100,
                        numberOfForks = 20,
                        description = "This is the first repository.",
                        language = "Kotlin"
                    ),
                    GithubRepositoryItem(
                        id = "456",
                        name = "Repository 2",
                        fullName = "owner/Repository 2",
                        htmlUrl = "https://github.com/owner/Repository2",
                        fork = true,
                        numberOfStar = 50,
                        numberOfForks = 10,
                        description = "This is the second repository.",
                        language = "Java"
                    ),
                    GithubRepositoryItem(
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
                )

                coEvery { githubApiService.getGithubRepositoryList(any(), any(), any()) } returns githubRepositoryItems
                val result = repositoryImpl.getNonForkedGithubRepositoryList("owner", 1, 10)
                result shouldBe Result.success(listOf(
                    GithubRepositoryItem(
                        id = "123",
                        name = "Repository 1",
                        fullName = "owner/Repository 1",
                        htmlUrl = "https://github.com/owner/Repository1",
                        fork = false,
                        numberOfStar = 100,
                        numberOfForks = 20,
                        description = "This is the first repository.",
                        language = "Kotlin"
                    ),
                    GithubRepositoryItem(
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
                ))
                coVerify { githubApiService.getGithubRepositoryList("owner", 1, 10) }
            }
        }
        context("when triggered and result failure") {
            it("should return Result.Failure") {
                val error = Exception()
                coEvery { githubApiService.getGithubRepositoryList(any(), any(), any()) } throws error
                val result = repositoryImpl.getNonForkedGithubRepositoryList("owner", 1, 10)
                result shouldBe Result.failure(error)
            }
        }
    }
})