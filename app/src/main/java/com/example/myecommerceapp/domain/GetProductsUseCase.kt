package com.example.myecommerceapp.domain

import com.example.myecommerceapp.data.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(refreshData: Boolean) =
        productRepository.getAllProducts(refreshData)
}