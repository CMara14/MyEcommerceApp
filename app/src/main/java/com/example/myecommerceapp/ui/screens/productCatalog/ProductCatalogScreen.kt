package com.example.myecommerceapp.ui.screens.productCatalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.ui.UIState
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import com.example.myecommerceapp.ui.components.ProductCard
import com.example.myecommerceapp.ui.screens.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogScreen(
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val productsViewModel: ProductCatalogViewModel = hiltViewModel()
    val uiState = productsViewModel.uiState
    val products by productsViewModel.filteredProducts.collectAsState()

    LaunchedEffect(Unit) {
        productsViewModel.loadProductsInitial()
    }

    val inputSearch by productsViewModel.inputSearch.collectAsState()
    val currentSortOrder by productsViewModel.currentSortOrder.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val filteredProducts by productsViewModel.filteredProducts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Products", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = White
                ),
                actions = {
                    PriceSortButton(
                        text = "Price",
                        isSelected = currentSortOrder != SortOrder.None,
                        onClick = {
                            productsViewModel.onSortOrderChanged(
                                when (currentSortOrder) {
                                    is SortOrder.None -> SortOrder.Ascending
                                    is SortOrder.Ascending -> SortOrder.Descending
                                    is SortOrder.Descending -> SortOrder.None
                                    else -> SortOrder.Ascending
                                }
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = when (currentSortOrder) {
                                    is SortOrder.Ascending -> Icons.Filled.ArrowUpward
                                    is SortOrder.Descending -> Icons.Filled.ArrowDownward
                                    is SortOrder.None -> Icons.AutoMirrored.Filled.Sort
                                    else -> Icons.AutoMirrored.Filled.Sort
                                },
                                contentDescription = "Sort Order",
                                tint = if (currentSortOrder != SortOrder.None) White else LightGrayText
                            )
                        }
                    )
                }
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        when (uiState) {
            is UIState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PinkPastel)
                }
            }

            is UIState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is UIState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {

                    OutlinedTextField(
                        value = inputSearch,
                        onValueChange = { productsViewModel.onInputSearchChanged(it) },
                        label = { Text("Search...", color = LightGrayText) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = LightGrayText
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = InputFieldColor,
                            unfocusedContainerColor = InputFieldColor,
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedBorderColor = PinkPastel,
                            unfocusedBorderColor = InputFieldColor,
                            focusedLabelColor = PinkPastel,
                            unfocusedLabelColor = LightGrayText,
                            cursorColor = PinkPastel,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(products) { product ->
                            val productQuantity by productsViewModel.getProductQuantityFlow(product.id)
                                .collectAsState()
                            ProductCard(
                                product = product,
                                currentQuantity = productQuantity,
                                onClick = { clickedProduct ->
                                    // TODO: add action when clicking on the product
                                },
                                onQuantityChange = { updatedProduct, newQuantity ->
                                    cartViewModel.addOrUpdateProduct(updatedProduct, newQuantity)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PriceSortButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.wrapContentWidth(),
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) PinkPastel else Color.Transparent,
            contentColor = if (isSelected) White else LightGrayText,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = LightGrayText.copy(alpha = 0.5f)
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            enabled = enabled
        ).copy(
            brush = if (isSelected) SolidColor(PinkPastel) else SolidColor(LightGrayText),
            width = 1.dp
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.width(4.dp))
        }
        Text(text)
    }
}
