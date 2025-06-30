package com.example.myecommerceapp.presentation.views.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import com.example.myecommerceapp.presentation.viewmodel.ProductCatalogViewModel
import com.example.myecommerceapp.presentation.viewmodel.SortOrder
import com.example.myecommerceapp.presentation.views.components.CategoryFilterChip
import com.example.myecommerceapp.presentation.views.components.ProductCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogScreen(
    viewModel: ProductCatalogViewModel = hiltViewModel(),
) {
    val products by viewModel.filteredProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val inputSearch by viewModel.inputSearch.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentSortOrder by viewModel.currentSortOrder.collectAsState()

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
                            viewModel.onSortOrderChanged(
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
                                    else ->  Icons.AutoMirrored.Filled.Sort
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PinkPastel)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {

                OutlinedTextField(
                    value = inputSearch,
                    onValueChange = { viewModel.onInputSearchChanged(it) },
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
                        .padding(vertical = 16.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp)
                ) {
                    lazyItems(categories) { category ->
                        CategoryFilterChip(
                            category = category,
                            isSelected = category == selectedCategory,
                            onClick = { viewModel.onCategorySelected(category) }
                        )
                    }
                }
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
                        ProductCard(product = product) {
                            // TODO: add action when clicking on the product
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
