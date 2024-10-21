package com.example.network

import com.example.network.model.GithubRepositoryItem
import com.example.network.model.SearchUserResponse
import com.example.network.model.User
import com.example.network.model.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("/users")
    suspend fun getUsers(
        @Query("since") lastFetchedId: Long,
        @Query("per_page") perPage: Int,
    ): List<User>
    @GET("/search/users")
    suspend fun getUsersWithQuery(
        @Query("q") query: String,
        @Query("page") page: Long,
        @Query("per_page") perPage: Int,
    ): SearchUserResponse

    @GET("/users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
    ): UserDetail

    @GET("/users/{username}/repos")
    suspend fun getGithubRepositoryList(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<GithubRepositoryItem>
}