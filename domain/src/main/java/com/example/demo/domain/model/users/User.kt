package com.example.demo.domain.model.users

data class User(
    val id: UserId,
    val login: String,
    val avatarUrl: String
)