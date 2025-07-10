package com.example.myecommerceapp.domain.model

import androidx.room.Entity

@Entity
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)
