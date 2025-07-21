package com.example.myecommerceapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myecommerceapp.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double = 0.0,
    val hasDrink: Boolean,
) {
    fun toDomain() = Product(id, name, description, imageUrl, price, hasDrink)
}