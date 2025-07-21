package com.example.myecommerceapp.data.remote

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): LoginResult
}