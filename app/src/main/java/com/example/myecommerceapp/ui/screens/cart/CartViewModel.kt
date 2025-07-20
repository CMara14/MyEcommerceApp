package com.example.myecommerceapp.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.repository.CartRepository
import com.example.myecommerceapp.data.repository.OrderRepository
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _cartItems: StateFlow<List<CartItem>> = cartRepository.getCartItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val cartItems: StateFlow<List<CartItem>> = _cartItems

    val totalProducts: StateFlow<Int> = _cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalGeneral: StateFlow<Double> = _cartItems.map { items ->
        items.sumOf { it.unitPrice * it.quantity }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "AR"))

    fun addOrUpdateProduct(product: Product, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(product.id, newQuantity)
        }
    }

    fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(productId, newQuantity)
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            cartRepository.removeProductFromCart(productId)
        }
    }

    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }

    fun proceedToCheckout() {
        viewModelScope.launch {
            try {
                val currentCartItems = _cartItems.value
                val currentTotalAmount = totalGeneral.value
                val currentTotalItems = totalProducts.value

                orderRepository.createOrder(
                    cartItems = currentCartItems,
                    totalAmount = currentTotalAmount,
                    totalItems = currentTotalItems
                )
                cartRepository.clearCart()
            } catch (e: Exception) {
                println("ERROR: ${e.message}")
            }
        }
    }
}
