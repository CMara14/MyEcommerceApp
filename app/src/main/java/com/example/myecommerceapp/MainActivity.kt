package com.example.myecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myecommerceapp.ui.theme.MyEcommerceAppTheme
import com.example.myecommerceapp.ui.screens.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.domain.model.BottomNavItem
import com.example.myecommerceapp.ui.navigation.AppNavigationRoutes
import com.example.myecommerceapp.ui.navigation.BottomNavRoutes
import com.example.myecommerceapp.ui.navigation.AppNavigation
import com.example.myecommerceapp.ui.screens.productCatalog.ProductCatalogViewModel
import com.example.myecommerceapp.ui.components.BottomNavigationBar
import com.example.myecommerceapp.ui.screens.cart.CartScreen
import com.example.myecommerceapp.ui.screens.cart.CartViewModel
import com.example.myecommerceapp.ui.screens.orders.OrdersScreen
import com.example.myecommerceapp.ui.screens.productCatalog.ProductCatalogScreen
import com.example.myecommerceapp.ui.screens.profile.ProfileScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyEcommerceAppTheme {
                val navController = rememberNavController()

                val startDestination = if (loginViewModel.isUserLoggedIn()) {
                    AppNavigationRoutes.MAIN_CONTENT_HOST_ROUTE
                } else {
                    AppNavigationRoutes.LOGIN_ROUTE
                }

                AppNavigation(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    startDestination = startDestination,
                    onLogoutGlobal = {
                        loginViewModel.logout()
                        navController.navigate(AppNavigationRoutes.LOGIN_ROUTE) {
                            popUpTo(AppNavigationRoutes.MAIN_CONTENT_HOST_ROUTE) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainActivityContent(
    loginViewModel: LoginViewModel,
    onLogoutGlobal: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedCartViewModel: CartViewModel = hiltViewModel()

    val bottomNavItems = listOf(
        BottomNavItem(BottomNavRoutes.HOME_ROUTE, Icons.Filled.Home, "Home"),
        BottomNavItem(BottomNavRoutes.ORDER_ROUTE, Icons.Filled.Store, "Orders"),
        BottomNavItem(BottomNavRoutes.CART_ROUTE, Icons.Filled.ShoppingCart, "Cart"),
        BottomNavItem(BottomNavRoutes.PROFILE_ROUTE, Icons.Filled.Person, "Profile")
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = bottomNavItems.indexOfFirst { it.route == currentRoute },
                onItemSelected = { index ->
                    val selectedItem = bottomNavItems[index]
                    bottomNavController.navigate(selectedItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(bottomNavController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavRoutes.HOME_ROUTE,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(BottomNavRoutes.HOME_ROUTE) {
                val productCatalogViewModel: ProductCatalogViewModel = hiltViewModel()
                ProductCatalogScreen(
                    viewModel = productCatalogViewModel,
                    cartViewModel = sharedCartViewModel
                )
            }
            composable(BottomNavRoutes.ORDER_ROUTE) {
                OrdersScreen()
            }
            composable(BottomNavRoutes.CART_ROUTE) {
                CartScreen(
                    viewModel = sharedCartViewModel,
                    navController = bottomNavController
                )
            }
            composable(BottomNavRoutes.PROFILE_ROUTE) {
                ProfileScreen(onLogout = onLogoutGlobal)
            }
        }
    }
}