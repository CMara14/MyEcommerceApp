package com.example.myecommerceapp.domain

import com.example.myecommerceapp.data.remote.LoginResult
import com.example.myecommerceapp.data.repository.AuthRepository
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return authRepository.login(email, password)
    }
}