package com.example.myecommerceapp.ui.views.screens.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    open val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    open val password: StateFlow<String> = _password.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    open val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    open val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _loginSuccess = MutableStateFlow(false)
    open val loginSuccess: StateFlow<Boolean> = _loginSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    open val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    open val isLoginButtonEnabled: StateFlow<Boolean> = combine(
        email, password, isLoading
    ) { currentEmail, currentPassword, currentIsLoading ->
        !currentIsLoading &&
                currentEmail.isNotBlank() &&
                currentPassword.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    open fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null
        _emailError.value = null
    }

    open fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _errorMessage.value = null
        _passwordError.value = null
    }

    open fun performLogin() {
        _errorMessage.value = null
        _emailError.value = null
        _passwordError.value = null

        val currentEmail = _email.value.trim()
        val currentPassword = _password.value.trim()

        var formValid = true

        if (currentEmail.isEmpty()) {
            _emailError.value = "El email no puede estar vacío"
            formValid = false
        }
        if (currentPassword.isEmpty()) {
            _passwordError.value = "La contraseña no puede estar vacía"
            formValid = false
        }

        if (currentEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _emailError.value = "Formato de email inválido"
            formValid = false
        }

        if (currentPassword.isNotEmpty() && currentPassword.length < 8) {
            _passwordError.value = "La contraseña debe tener al menos 8 caracteres"
            formValid = false
        }

        if (!formValid) {
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val loginResult = authRepository.login(currentEmail, currentPassword)

            if (loginResult) {
                authRepository.setLoggedIn(true)
                _loginSuccess.value = true
            } else {
                _errorMessage.value = "Email o contraseña incorrectos."
            }
            _isLoading.value = false
        }
    }

    open fun clearErrorMessage() {
        _errorMessage.value = null
    }
    open fun resetLoginSuccess() {
        _loginSuccess.value = false
    }

    open fun isUserLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    open fun logout() {
        authRepository.setLoggedIn(false)
    }
}
