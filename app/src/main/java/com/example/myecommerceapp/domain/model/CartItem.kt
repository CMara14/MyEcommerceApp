package com.example.myecommerceapp.domain.model

data class CartItem(
    val productId: String,
    val name: String,
    val imageUrl: String,
    val unitPrice: Double,
    var quantity: Int
)