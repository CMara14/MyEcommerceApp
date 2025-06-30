package com.example.myecommerceapp.presentation.views.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.White
import com.example.myecommerceapp.presentation.viewmodel.LoginViewModel
import androidx.compose.ui.unit.sp
import com.example.myecommerceapp.ui.theme.ActiveButton
import com.example.myecommerceapp.ui.theme.DisabledButton
import com.example.myecommerceapp.ui.theme.DisabledTextButton
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val isLoginButtonEnabled by viewModel.isLoginButtonEnabled.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onLoginSuccess()
            viewModel.resetLoginSuccess()
        }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearErrorMessage()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = DarkBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.headlineLarge.copy(color = White),
                    modifier = Modifier.padding(vertical = 32.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = { Text("Email", color = LightGrayText) },
                    isError = emailError != null,
                    supportingText = { if (emailError != null) Text(emailError!!) },
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Password", color = LightGrayText) },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError != null,
                    supportingText = { if (passwordError != null) Text(passwordError!!) },
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
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.performLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ActiveButton,
                        contentColor = White,
                        disabledContainerColor = DisabledButton,
                        disabledContentColor = DisabledTextButton
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = isLoginButtonEnabled
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Login")
                    }
                }
            }

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "Don't have an account? Sign Up",
                    color = LightGrayText,
                    fontSize = 14.sp
                )
            }
        }
    }
}

