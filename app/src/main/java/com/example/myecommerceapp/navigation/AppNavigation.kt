package com.example.myecommerceapp.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myecommerceapp.presentation.views.screens.LoginScreen
import com.example.myecommerceapp.presentation.views.screens.RegisterScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.presentation.viewmodel.LoginViewModel
import com.example.myecommerceapp.presentation.viewmodel.RegisterViewModel
import com.example.myecommerceapp.MainActivityContent

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    startDestination: String,
    onLogoutGlobal: () -> Unit
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(AppNavigationRoutes.LOGIN_ROUTE) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(AppNavigationRoutes.MAIN_CONTENT_HOST_ROUTE) {
                        popUpTo(AppNavigationRoutes.LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppNavigationRoutes.REGISTER_ROUTE) {
                        popUpTo(AppNavigationRoutes.LOGIN_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(AppNavigationRoutes.REGISTER_ROUTE) {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = registerViewModel,
                onRegisterSuccess = {
                    navController.navigate(AppNavigationRoutes.LOGIN_ROUTE) {
                        popUpTo(AppNavigationRoutes.REGISTER_ROUTE) { inclusive = true }
                    }
                    Toast.makeText(context, "¡Registro completado! Por favor, inicia sesión.", Toast.LENGTH_LONG).show()
                },
                onNavigateToLogin = {
                    navController.navigate(AppNavigationRoutes.LOGIN_ROUTE) {
                        popUpTo(AppNavigationRoutes.REGISTER_ROUTE) { inclusive = true }
                    }
                }
            )
        }

        composable(AppNavigationRoutes.MAIN_CONTENT_HOST_ROUTE) {
            MainActivityContent(
                loginViewModel = loginViewModel,
                onLogoutGlobal = onLogoutGlobal
            )
        }
    }
}