package com.example.myecommerceapp.domain.model

data class Order(
    val id: Int,
    val orderDate: Long,
    val totalAmount: Double,
    val totalItems: Int,
    val items: List<OrderItem>
)