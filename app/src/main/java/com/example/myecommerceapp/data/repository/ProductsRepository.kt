package com.example.myecommerceapp.data.repository
import com.example.myecommerceapp.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
}
