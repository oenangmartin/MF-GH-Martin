package com.example.githubexplorer.repository

import com.example.githubexplorer.helper.TestDispatcherProvider
import com.example.network.GithubApiService
import com.example.network.exception.CommonError
import com.example.network.exception.NoInternetException
import com.example.network.model.SearchUserResponse
import com.example.network.model.User
import com.example.network.model.UserDetail
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okio.IOException
import retrofit2.HttpException

class UserRepositoryImplTest : DescribeSpec({
    lateinit var repositoryImpl: UserRepositoryImpl
    val apiService = mockk<GithubApiService>()
    val dispatcherProvider = TestDispatcherProvider()

    beforeEach {
        repositoryImpl = UserRepositoryImpl(apiService, dispatcherProvider)
    }

    describe("getUserList") {
        context("when triggered and result success") {
            it("should return Result.Success with correct data") {
                val mockList = mockk<List<User>>()
                coEvery { apiService.getUsers(any(), any()) } returns mockList
                val result = repositoryImpl.getUserList(1, 10)
                result shouldBe Result.success(mockList)
            }
        }
        context("when triggered and result failure") {
            it("should return Result.Failure") {
                val error = IOException()
                val expectedError = NoInternetException
                coEvery { apiService.getUsers(any(), any()) } throws error
                val result = repositoryImpl.getUserList(1, 10)
                result shouldBe Result.failure(expectedError)
                coVerify { apiService.getUsers(1, 10) }
            }
        }
    }
    describe("getUserListWithQuery") {
        context("when triggered and result success") {
            it("should return Result.Success with correct data") {
                val mockResponse = mockk<SearchUserResponse>()
                val mockList = mockk<List<User>>()
                every { mockResponse.items } returns mockList
                coEvery { apiService.getUsersWithQuery(any(), any(), any()) } returns mockResponse
                val result = repositoryImpl.getUserListWithQuery("query", 1, 10)
                result shouldBe Result.success(mockList)
                coVerify { apiService.getUsersWithQuery("query", 1, 10) }
            }
        }

        context("when triggered and result failed") {
            it("should return Result.failure") {
                val error = mockk<HttpException>()
                every { error.code() } returns 400
                every { error.message() } returns "message"
                coEvery { apiService.getUsersWithQuery(any(), any(), any()) } throws error
                val result = repositoryImpl.getUserListWithQuery("query", 1, 10)
                result shouldBe Result.failure(CommonError(400, "message"))
                coVerify { apiService.getUsersWithQuery("query", 1, 10) }
            }
        }
    }
    describe("getUserDetail") {
        context("when triggered and result success") {
            it("should return Result.Success with correct data") {
                val mockUserDetail = mockk<UserDetail>()
                coEvery { apiService.getUserDetail(any()) } returns mockUserDetail
                val result = repositoryImpl.getUserDetail("username")
                result shouldBe Result.success(mockUserDetail)
                coVerify { apiService.getUserDetail("username") }
            }
        }
        context("when triggered and result failed") {
            it("should return Result.failure") {
                val error = Exception()
                coEvery { apiService.getUserDetail(any()) } throws error
                val result = repositoryImpl.getUserDetail("username")
                result shouldBe Result.failure(error)
                coVerify { apiService.getUserDetail("username") }
            }
        }
    }
})