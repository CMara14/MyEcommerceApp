package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.remote.LoginResult
import com.example.myecommerceapp.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): LoginResult
    fun isLoggedIn(): Boolean
    fun setLoggedIn(isLoggedIn: Boolean)
    fun isEmailRegistered(email: String): Boolean
    suspend fun register(email: String, name: String, password: String): Boolean
    suspend fun getCurrentUserProfile(): User?
    suspend fun updateUserProfile(user: User): Boolean
}


