package com.example.githubexplorer.ui.userdetail

import com.example.githubexplorer.helper.TestDispatcherProvider
import com.example.githubexplorer.repository.UserRepository
import com.example.network.model.UserDetail
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class UserDetailViewModelTest : DescribeSpec({
    val username = "username"
    val userRepository = mockk<UserRepository>()
    val userDetailMapper = mockk<com.example.githubexplorer.ui.userdetail.UserDetailMapper>()
    val dispatcherProvider = TestDispatcherProvider()

    describe("on initialize") {
        it("should fetch initial data") {
            runTest {
                val mockUserDetail = mockk<UserDetail>()
                val mockUserDetailDisplayData = mockk<com.example.githubexplorer.ui.userdetail.UserDetailDisplayData>()
                every { userDetailMapper.map(mockUserDetail) } returns mockUserDetailDisplayData
                coEvery { userRepository.getUserDetail("username") } returns Result.success(mockUserDetail)

                val viewModel =
                    UserDetailViewModel(username, userRepository, userDetailMapper, dispatcherProvider)

                coVerify { userRepository.getUserDetail("username") }

                viewModel.uiState.value shouldBe UserDetailUiState.Content(
                    userDetailDisplayData = mockUserDetailDisplayData
                )
            }
        }
    }
})