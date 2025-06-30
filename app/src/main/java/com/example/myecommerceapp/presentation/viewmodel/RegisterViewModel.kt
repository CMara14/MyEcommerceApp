package com.example.myecommerceapp.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _fullName = MutableStateFlow("")
    open val fullName: StateFlow<String> = _fullName.asStateFlow()

    private val _email = MutableStateFlow("")
    open val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    open val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    open val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _fullNameError = MutableStateFlow<String?>(null)
    open val fullNameError: StateFlow<String?> = _fullNameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    open val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    open val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    open val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    open val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    open val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    open val isRegisterButtonEnabled: StateFlow<Boolean> = combine(
        fullName, email, password, confirmPassword, isLoading
    ) { currentFullName, currentEmail, currentPassword, currentConfirmPassword, currentIsLoading ->
        !currentIsLoading &&
                currentFullName.isNotBlank() &&
                currentEmail.isNotBlank() &&
                currentPassword.isNotBlank() &&
                currentConfirmPassword.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    open fun onFullNameChanged(newName: String) {
        _fullName.value = newName
        _errorMessage.value = null
        _fullNameError.value = null
    }

    open fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        _errorMessage.value = null
        _emailError.value = null
    }

    open fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        _errorMessage.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    open fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _errorMessage.value = null
        _confirmPasswordError.value = null
    }

    open fun performRegistration() {
        _errorMessage.value = null
        _fullNameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null

        val currentFullName = _fullName.value.trim()
        val currentEmail = _email.value.trim()
        val currentPassword = _password.value.trim()
        val currentConfirmPassword = _confirmPassword.value.trim()

        var formValid = true

        if (currentEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _emailError.value = "Formato de email inválido"
            formValid = false
        }

        if (currentPassword.isNotEmpty() && currentPassword.length < 8) {
            _passwordError.value = "La contraseña debe tener al menos 8 caracteres"
            formValid = false
        }

        if (currentPassword.isNotEmpty() && currentConfirmPassword.isNotEmpty() && currentPassword != currentConfirmPassword) {
            _confirmPasswordError.value = "Las contraseñas no coinciden"
            formValid = false
        }

        if (!formValid) {
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            if (authRepository.isEmailRegistered(currentEmail)) {
                _errorMessage.value = "El email ya está registrado."
                _isLoading.value = false
                return@launch
            }

            val registrationResult = authRepository.register(currentEmail, currentFullName, currentPassword)

            if (registrationResult) {
                println("Usuario $currentEmail registrado en memoria compartida.")
                _registerSuccess.value = true
            } else {
                _errorMessage.value = "Error al registrar el usuario."
            }
            _isLoading.value = false
        }
    }

    open fun resetRegisterSuccess() {
        _registerSuccess.value = false
    }
}
