package com.example.demo.data.api

import com.example.demo.data.model.github.SearchedUsersDTO
import com.example.demo.data.model.github.UserDTO
import com.example.demo.data.model.github.UserDetailsDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface GithubApi {

    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<UserDTO>

    @GET("users/{login}")
    suspend fun getUserDetails(
        @Path("login") login: String
    ): UserDetailsDTO

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchedUsersDTO
}