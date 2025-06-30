package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.model.User

interface AuthRepository {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(isLoggedIn: Boolean)
    fun isEmailRegistered(email: String): Boolean
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(email: String, name: String, password: String): Boolean
    suspend fun getCurrentUserProfile(): User?
    suspend fun updateUserProfile(user: User): Boolean
}


