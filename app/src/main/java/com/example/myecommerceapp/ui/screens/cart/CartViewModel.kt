package com.example.myecommerceapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    val totalProducts: StateFlow<Int> = MutableStateFlow(0)
    val totalGeneral: StateFlow<Double> = MutableStateFlow(0.0)

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "AR"))

    init {
        viewModelScope.launch {
            _cartItems.collect { items ->
                (totalProducts as MutableStateFlow).value = items.sumOf { it.quantity }
                (totalGeneral as MutableStateFlow).value =
                    items.sumOf { it.unitPrice * it.quantity }
            }
        }
    }

    fun addOrUpdateProduct(product: Product, newQuantity: Int) {
        _cartItems.update { currentItems ->
            val existingItem = currentItems.find { it.productId == product.id }

            val updatedList = currentItems.toMutableList()

            if (newQuantity == 0) {
                existingItem?.let {
                    updatedList.remove(it)
                }
            } else {
                if (existingItem != null) {
                    val index = updatedList.indexOf(existingItem)
                    updatedList[index] = existingItem.copy(quantity = newQuantity)
                } else {
                    updatedList.add(
                        CartItem(
                            productId = product.id,
                            name = product.name,
                            imageUrl = product.imageUrl,
                            unitPrice = product.price,
                            quantity = newQuantity
                        )
                    )
                }
            }
            updatedList.toList()
        }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        _cartItems.update { currentItems ->
            val updatedList = currentItems.toMutableList()
            val index = updatedList.indexOfFirst { it.productId == productId }

            if (index != -1) {
                if (newQuantity == 0) {
                    updatedList.removeAt(index)
                } else {
                    updatedList[index] = updatedList[index].copy(quantity = newQuantity)
                }
            }
            updatedList.toList()
        }
    }

    fun removeItem(productId: String) {
        _cartItems.update { currentItems ->
            val updatedList = currentItems.toMutableList()
            updatedList.removeIf { it.productId == productId }
            updatedList.toList()
        }
    }


    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }

    fun proceedToCheckout() {
        // TODO: Implement checkout logic here.
    }
}