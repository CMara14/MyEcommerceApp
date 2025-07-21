package com.example.myecommerceapp.data.repository

import android.content.Context
import com.example.myecommerceapp.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import com.example.myecommerceapp.data.remote.AuthRemoteDataSource
import com.example.myecommerceapp.data.remote.LoginRequestDto
import com.example.myecommerceapp.data.remote.LoginResult

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    private val registeredUsers: MutableList<User> = mutableListOf(
        User("test@test.com", "12345678")
    )
    private var _isLoggedIn: Boolean = false
    private var _loggedInUserEmail: String? = null

    override fun isLoggedIn(): Boolean {
        return _isLoggedIn
    }

    override fun setLoggedIn(loggedIn: Boolean) {
        _isLoggedIn = loggedIn
        if (!loggedIn) {
            _loggedInUserEmail = null
        }
    }

    private fun getLoggedInUserEmail(): String? {
        return _loggedInUserEmail
    }

    private fun setLoggedInUserEmail(email: String) {
        _loggedInUserEmail = email
    }

    override suspend fun login(email: String, password: String): LoginResult {
        val requestBody = LoginRequestDto(email, password)
        val result = authRemoteDataSource.login(requestBody)

        when (result) {
            is LoginResult.Success -> {
                setLoggedIn(true)
                setLoggedInUserEmail(email)
            }
            else -> {
                setLoggedIn(false)
            }
        }
        return result
    }

    override suspend fun register(email: String, password: String, name: String): Boolean {
        delay(1000)
        if (registeredUsers.any { it.email == email }) {
            return false
        }

        val newUser = User(email, password, name,"","")
        registeredUsers.add(newUser)
        return true
    }

    override fun isEmailRegistered(email: String): Boolean {
        val isRegistered = registeredUsers.any { it.email == email }
        return isRegistered
    }

    override suspend fun getCurrentUserProfile(): User? {
        delay(500)
        val loggedInEmail = getLoggedInUserEmail()
        return registeredUsers.find { it.email == loggedInEmail }
    }

    override suspend fun updateUserProfile(user: User): Boolean {
        delay(1000)

        val index = registeredUsers.indexOfFirst { it.email == user.email }
        return if (index != -1) {
            registeredUsers[index] = user
            true
        } else {
            false
        }
    }
}
