package com.example.myecommerceapp.presentation.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myecommerceapp.data.model.CartItem
import com.example.myecommerceapp.presentation.views.components.CartItemCard
import com.example.myecommerceapp.ui.theme.DarkBackground
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.MyEcommerceAppTheme
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White
import java.text.DecimalFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen() {
    val mockCartItems = remember { mutableStateListOf(
        CartItem("smw001", "Smartwatch Fit Pro", "https://images.unsplash.com/photo-1638798486151-f5247b3c7261?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 65000.0, 1),
        CartItem("head002", "Auriculares Bluetooth", "https://images.unsplash.com/photo-1705614055003-d2c0b47d098d?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 80500.0, 2),
        CartItem("shirt003", "Camiseta Deportiva DryFit", "https://images.unsplash.com/photo-1737094547812-1499f4b700a0?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 25000.0, 1),
        CartItem("perf004", "Perfume Chanel N° 5", "https://images.unsplash.com/photo-1631701464241-99f7f0ed6f8f?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 350000.0, 1),
    )}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", color = White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = White
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mockCartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your cart is empty.", color = LightGrayText, fontSize = 18.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(mockCartItems) { item ->
                        CartItemCard(
                            item = item,
                            onQuantityChange = { productId, newQuantity ->
                                val index = mockCartItems.indexOfFirst { it.productId == productId }
                                if (index != -1) {
                                    mockCartItems[index] = mockCartItems[index].copy(quantity = newQuantity)
                                }
                            },
                            onRemoveItem = { productId ->
                                mockCartItems.removeIf { it.productId == productId }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                CartSummary(cartItems = mockCartItems)
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* TODO: Implement checkout logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkPastel,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Proceed to Checkout")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CartSummary(cartItems: List<CartItem>) {
    val totalProducts = cartItems.sumOf { it.quantity }
    val totalGeneral = cartItems.sumOf { it.unitPrice * it.quantity }

    val currencyFormatter = remember { DecimalFormat("$#,##0.00", java.text.DecimalFormatSymbols(Locale.US)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(InputFieldColor)
            .padding(16.dp)
    ) {
        Text(
            text = "Order Summary",
            style = MaterialTheme.typography.titleMedium.copy(color = White),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total Products:", color = LightGrayText)
            Text("$totalProducts", color = White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total:", color = LightGrayText)
            Text(currencyFormatter.format(totalGeneral), color = PinkPastel, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    MyEcommerceAppTheme {
        CartScreen()
    }
}