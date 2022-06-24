package com.example.demo.domain.usecase

import com.example.demo.domain.model.users.UserDetails
import com.example.demo.domain.repository.UserRepository

class GetUserDetailsUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(login: String): Result<UserDetails> {
        return userRepository.getUserDetails(login)
    }
}