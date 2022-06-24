package com.example.demo.data.mapper

import com.example.demo.data.model.github.UserDTO
import com.example.demo.data.model.github.UserDetailsDTO
import com.example.demo.data.model.github.UserEntity
import com.example.demo.domain.model.users.User
import com.example.demo.domain.model.users.UserDetails
import com.example.demo.domain.model.users.UserId

internal fun UserDTO.toDomainModel(): User {
    return User(
        id = UserId(id),
        login = login,
        avatarUrl = avatarUrl
    )
}

internal fun UserEntity.toDomainModel(): User {
    return User(
        id = UserId(id),
        login = login,
        avatarUrl = avatarUrl
    )
}

internal fun UserDetailsDTO.toDomainModel(): UserDetails {
    return UserDetails(
        id = UserId(id),
        login = login,
        avatarUrl = avatarUrl,
        followers = followers,
        following = following
    )
}

internal fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id.rawId,
        login = login,
        avatarUrl = avatarUrl
    )
}