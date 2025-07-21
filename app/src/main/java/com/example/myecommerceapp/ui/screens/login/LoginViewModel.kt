package com.example.myecommerceapp.ui.screens.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.remote.LoginResult
import com.example.myecommerceapp.data.repository.AuthRepository
import com.example.myecommerceapp.domain.PostLoginUseCase
import com.example.myecommerceapp.ui.UIState
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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val loginUseCase: PostLoginUseCase
) : ViewModel() {
    private val _loginUiState = MutableStateFlow<UIState<Boolean>>(UIState.Success(false))
    open val loginUiState: StateFlow<UIState<Boolean>> = _loginUiState.asStateFlow()

    private val _email = MutableStateFlow("")
    open val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    open val password: StateFlow<String> = _password.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    open val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    open val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    open val isLoginButtonEnabled: StateFlow<Boolean> = combine(
        email, password, loginUiState, _emailError, _passwordError
    ) { currentEmail, currentPassword, currentUiState, emailErr, passErr ->
        currentUiState !is UIState.Loading &&
                currentEmail.isNotBlank() && emailErr == null &&
                currentPassword.isNotBlank() && passErr == null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    open fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
        if (_loginUiState.value is UIState.Error) {
            _loginUiState.value = UIState.Success(false)
        }
    }

    open fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
        if (_loginUiState.value is UIState.Error) {
            _loginUiState.value = UIState.Success(false)
        }
    }

    open fun performLogin() {
        _emailError.value = null
        _passwordError.value = null
        _loginUiState.value = UIState.Loading
        val currentEmail = _email.value.trim()
        val currentPassword = _password.value.trim()

        var formValid = true

        if (currentEmail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _emailError.value = "Invalid email format"; formValid = false
        }
        if (currentPassword.isNotEmpty() && currentPassword.length < 8) {
            _passwordError.value = "The password must be at least 8 characters long."; formValid =
                false
        }

        if (!formValid) {
            return
        }

        viewModelScope.launch {
            when (val result = loginUseCase(currentEmail, currentPassword)) {
                is LoginResult.Success -> {
                    authRepository.setLoggedIn(true)
                    _loginUiState.value = UIState.Success(true)
                }

                is LoginResult.NetworkError -> {
                    _loginUiState.value = UIState.Error("Connection failed")
                }

                is LoginResult.UnknownError -> {
                    _loginUiState.value = UIState.Error(result.message)
                }
            }
        }
    }

    open fun resetLoginUiStateToInitial() {
        _loginUiState.value = UIState.Success(false)
    }

    open fun isUserLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    open fun logout() {
        authRepository.setLoggedIn(false)
    }
}
