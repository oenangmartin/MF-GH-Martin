package com.example.githubexplorer.repository

import com.example.network.model.User
import com.example.network.model.UserDetail

interface UserRepository {
    suspend fun getUserList(page: Long, pageSize: Int): Result<List<User>>
    suspend fun getUserListWithQuery(query: String, page: Long, pageSize: Int): Result<List<User>>
    suspend fun getUserDetail(username: String): Result<UserDetail>
}