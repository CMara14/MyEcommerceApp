package com.example.myecommerceapp.data.remote

import com.example.myecommerceapp.data.local.entities.ProductEntity
import com.example.myecommerceapp.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val description: String = "General",
    val imageUrl: String,
    val price: Double = 0.0,
    val hasDrink: Boolean = false,
) {
    fun toDomain() = Product(
        id,
        name,
        description,
        imageUrl,
        price,
        hasDrink
    )
    fun toEntity() = ProductEntity(
        id,
        name,
        description,
        imageUrl,
        price,
        hasDrink
    )
}
