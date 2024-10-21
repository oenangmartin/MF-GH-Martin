package com.example.githubexplorer.ui.userdetail.ghrepository

import com.example.githubexplorer.common.SnackbarState
import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.githubexplorer.helper.TestDispatcherProvider
import com.example.githubexplorer.repository.GithubRepoRepository
import com.example.network.exception.NoInternetException
import com.example.network.model.GithubRepositoryItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class GithubRepositoryListViewModelTest : DescribeSpec({

    val username = "username"
    val repository: GithubRepoRepository = mockk()
    val itemMapper: GithubRepositoryItemMapper = mockk()
    val dispatcherProvider: DispatcherProvider = TestDispatcherProvider()

    fun initializeViewModel(pageSize: Int = 20): GithubRepositoryListViewModel {
        return GithubRepositoryListViewModel(
            pageSize = pageSize,
            username = username,
            repository = repository,
            itemMapper = itemMapper,
            dispatcherProvider = dispatcherProvider
        )
    }

    describe("on initialized") {
        context("item exists") {
            it("should load next item") {
                val mockList = listOf(
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
                    )
                )
                val mockMappedData = mockk<GithubRepositoryItemDisplayData>()
                coEvery {
                    repository.getNonForkedGithubRepositoryList(
                        "username",
                        any(),
                        any()
                    )
                } returns Result.success(mockList)
                every { itemMapper.map(any()) } returns mockMappedData

                val viewModel = initializeViewModel()
                viewModel.uiState.value shouldBe GithubRepositoryListUiState.Content(
                    isLoading = false,
                    repositories = listOf(mockMappedData)
                )
            }
        }
        context("item empty") {
            it("should show error") {
                coEvery {
                    repository.getNonForkedGithubRepositoryList(
                        "username",
                        any(),
                        any()
                    )
                } returns Result.success(emptyList())

                val viewModel = initializeViewModel()
                viewModel.uiState.value shouldBe GithubRepositoryListUiState.Error("No repositories from this user")
            }
        }
    }
    describe("loadMore") {
        context("when has reached end") {
            it("should not load next item") {
                val mockList1 = listOf(
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
                    )
                )
                val mockMappedData = mockk<GithubRepositoryItemDisplayData>()
                coEvery {
                    repository.getNonForkedGithubRepositoryList(
                        "username",
                        1,
                        any()
                    )
                } returns Result.success(mockList1)

                every { itemMapper.map(mockList1[0]) } returns mockMappedData

                val viewModel = initializeViewModel()
                viewModel.loadMore()
                viewModel.uiState.value shouldBe GithubRepositoryListUiState.Content(
                    isLoading = false,
                    repositories = listOf(mockMappedData)
                )
            }
        }

        context("when hasn't reached end") {
            context("and successfully load next item") {
                it("should load more data") {
                    runTest {
                        val pageSize = 2
                        val mockList = listOf(
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
                                id = "321",
                                name = "Repository 2",
                                fullName = "owner/Repository 2",
                                htmlUrl = "https://github.com/owner/Repository2",
                                fork = false,
                                numberOfStar = 101,
                                numberOfForks = 22,
                                description = "This is the second repository.",
                                language = "Kotlin"
                            ),
                        )
                        val mockedDisplayData = mutableListOf<GithubRepositoryItemDisplayData>()
                        coEvery {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                1,
                                any()
                            )
                        } returns Result.success(mockList)
                        val mockList2 = listOf(
                            GithubRepositoryItem(
                                id = "1233",
                                name = "Repository 3",
                                fullName = "owner/Repository 3",
                                htmlUrl = "https://github.com/owner/Repository3",
                                fork = false,
                                numberOfStar = 100,
                                numberOfForks = 20,
                                description = "This is the first repository.",
                                language = "Kotlin"
                            ),
                        )
                        coEvery {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                2,
                                any()
                            )
                        } returns Result.success(mockList2)

                        mockList.forEach {
                            every { itemMapper.map(it) } returns mockk<GithubRepositoryItemDisplayData>().apply {
                                mockedDisplayData.add(this)
                            }
                        }
                        mockList2.forEach {
                            every { itemMapper.map(it) } returns mockk<GithubRepositoryItemDisplayData>().apply {
                                mockedDisplayData.add(this)
                            }
                        }

                        val viewModel = initializeViewModel(pageSize)
                        viewModel.loadMore()
                        viewModel.uiState.value shouldBe GithubRepositoryListUiState.Content(
                            isLoading = false,
                            repositories = mockedDisplayData
                        )
                        coVerify {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                1,
                                pageSize
                            )
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                2,
                                pageSize
                            )
                        }
                    }
                }
            }
            context("and failed load next item") {
                it("should load more data") {
                    runTest {
                        val pageSize = 2
                        val mockList = listOf(
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
                                id = "321",
                                name = "Repository 2",
                                fullName = "owner/Repository 2",
                                htmlUrl = "https://github.com/owner/Repository2",
                                fork = false,
                                numberOfStar = 101,
                                numberOfForks = 22,
                                description = "This is the second repository.",
                                language = "Kotlin"
                            ),
                        )
                        val mockedDisplayData = mutableListOf<GithubRepositoryItemDisplayData>()
                        coEvery {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                1,
                                any()
                            )
                        } returns Result.success(mockList)
                        coEvery {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                2,
                                any()
                            )
                        } returns Result.failure(NoInternetException)

                        mockList.forEach {
                            every { itemMapper.map(it) } returns mockk<GithubRepositoryItemDisplayData>().apply {
                                mockedDisplayData.add(this)
                            }
                        }
                        val viewModel = initializeViewModel(pageSize)
                        viewModel.loadMore()
                        viewModel.uiState.value shouldBe GithubRepositoryListUiState.Content(
                            isLoading = false,
                            repositories = mockedDisplayData
                        )
                        viewModel.snackbarState.value shouldBe SnackbarState("No internet connection")
                        coVerify {
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                1,
                                pageSize
                            )
                            repository.getNonForkedGithubRepositoryList(
                                "username",
                                2,
                                pageSize
                            )
                        }
                    }
                }
            }
        }

        context("when api failed") {
            context("and currentList is empty") {
                it("Should show error") {
                    coEvery {
                        repository.getNonForkedGithubRepositoryList(
                            "username",
                            any(),
                            any()
                        )
                    } returns Result.failure(NoInternetException)
                    val viewModel = initializeViewModel()
                    viewModel.uiState.value shouldBe GithubRepositoryListUiState.Error("No internet connection")
                }
            }
        }
    }
    describe("setSnackbarMessageShown") {
        it("should reset snackbar state") {
            val viewModel = initializeViewModel()
            viewModel.setSnackbarMessageShown()
            viewModel.snackbarState.value shouldBe SnackbarState("")
        }
    }
})