package com.example.demo.domain.model.users

data class UserDetails(
    val id: UserId,
    val login: String,
    val avatarUrl: String,
    val followers: Int,
    val following: Int
)