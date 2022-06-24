package com.example.demo.domain.usecase

import com.example.demo.domain.model.users.User
import com.example.demo.domain.repository.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(since: Int, perPage: Int): Result<List<User>> {
        return userRepository.getUsers(since, perPage)
    }
}