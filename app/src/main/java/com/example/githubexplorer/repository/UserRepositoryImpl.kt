package com.example.githubexplorer.repository

import com.example.githubexplorer.coroutine.DispatcherProvider
import com.example.network.GithubApiService
import com.example.network.helper.safeApiCall
import com.example.network.model.User
import com.example.network.model.UserDetail

class UserRepositoryImpl(
    private val apiService: GithubApiService,
    private val dispatcherProvider: DispatcherProvider,
) : UserRepository {

    override suspend fun getUserList(page: Long, pageSize: Int): Result<List<User>> {
        return safeApiCall(dispatcherProvider.io()) {
            apiService.getUsers(page, pageSize)
        }
    }

    override suspend fun getUserListWithQuery(
        query: String,
        page: Long,
        pageSize: Int
    ): Result<List<User>> {
        return safeApiCall(dispatcherProvider.io()) {
            apiService.getUsersWithQuery(query, page, pageSize).items
        }
    }

    override suspend fun getUserDetail(username: String): Result<UserDetail> {
        return safeApiCall(dispatcherProvider.io()) {
            apiService.getUserDetail(username)
        }
    }
}