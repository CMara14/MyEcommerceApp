package com.example.myecommerceapp.domain.model

data class CartItem(
    val id: Int = 0,
    val productId: String,
    val name: String,
    val imageUrl: String,
    val unitPrice: Double,
    val quantity: Int,
)