package com.example.myecommerceapp.data.remote

import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

sealed class LoginResult {
    object Success : LoginResult()
    object NetworkError : LoginResult()
    data class UnknownError(val message: String) : LoginResult()
}


@Singleton
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRemoteDataSource {
    override suspend fun login(request: LoginRequestDto): LoginResult {
        return try {
            apiService.loginUser(request)
            LoginResult.Success
        } catch (e: IOException) {
            LoginResult.NetworkError
        } catch (e: Exception) {
            LoginResult.UnknownError(e.message ?: "Unexpected error occurred")
        }
    }
}
