package com.example.demo.domain.repository

import com.example.demo.domain.model.users.User
import com.example.demo.domain.model.users.UserDetails

interface UserRepository {

    suspend fun getUsers(since: Int, perPage: Int): Result<List<User>>

    suspend fun searchUsers(query: String, page: Int, perPage: Int): Result<List<User>>

    suspend fun getUserDetails(login: String): Result<UserDetails>
}