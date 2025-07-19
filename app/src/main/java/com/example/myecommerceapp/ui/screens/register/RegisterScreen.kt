package com.example.myecommerceapp.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.ui.theme.ActiveButton
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.DisabledButton
import com.example.myecommerceapp.ui.theme.DisabledTextButton
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.White

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val fullName by viewModel.fullName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()

    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val fullNameError by viewModel.fullNameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()

    val isRegisterButtonEnabled by viewModel.isRegisterButtonEnabled.collectAsState()

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            onRegisterSuccess()
            viewModel.resetRegisterSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium.copy(color = White),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = { viewModel.onFullNameChanged(it) },
                label = { Text("Full Name", color = LightGrayText) },
                isError = fullNameError != null,
                supportingText = { if (fullNameError != null) Text(fullNameError!!) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputFieldColor,
                    unfocusedContainerColor = InputFieldColor,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = ActiveButton,
                    unfocusedBorderColor = InputFieldColor,
                    focusedLabelColor = White,
                    unfocusedLabelColor = LightGrayText,
                    cursorColor = ActiveButton,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email", color = LightGrayText) },
                isError = emailError != null,
                supportingText = { if (emailError != null) Text(emailError!!) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputFieldColor,
                    unfocusedContainerColor = InputFieldColor,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = ActiveButton,
                    unfocusedBorderColor = InputFieldColor,
                    focusedLabelColor = White,
                    unfocusedLabelColor = LightGrayText,
                    cursorColor = ActiveButton,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password", color = LightGrayText) },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { if (passwordError != null) Text(passwordError!!) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputFieldColor,
                    unfocusedContainerColor = InputFieldColor,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = ActiveButton,
                    unfocusedBorderColor = InputFieldColor,
                    focusedLabelColor = White,
                    unfocusedLabelColor = LightGrayText,
                    cursorColor = ActiveButton,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = { Text("Confirm Password", color = LightGrayText) },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = { if (confirmPasswordError != null) Text(confirmPasswordError!!) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputFieldColor,
                    unfocusedContainerColor = InputFieldColor,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedBorderColor = ActiveButton,
                    unfocusedBorderColor = InputFieldColor,
                    focusedLabelColor = White,
                    unfocusedLabelColor = LightGrayText,
                    cursorColor = ActiveButton,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = { viewModel.performRegistration() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ActiveButton,
                    contentColor = White,
                    disabledContainerColor = DisabledButton,
                    disabledContentColor = DisabledTextButton
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = isRegisterButtonEnabled
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Register", fontSize = 20.sp)
                }
            }

            TextButton(onClick = onNavigateToLogin) {
                Text(
                    text = "Already have an account? Sign In",
                    color = LightGrayText,
                    fontSize = 14.sp
                )
            }
        }
    }
}

