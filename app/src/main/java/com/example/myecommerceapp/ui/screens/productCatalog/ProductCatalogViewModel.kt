package com.example.myecommerceapp.ui.screens.productCatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.repository.CartRepository
import com.example.myecommerceapp.data.repository.ProductRepository
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*

sealed class SortOrder {
    data object None : SortOrder()
    data object Ascending : SortOrder()
    data object Descending : SortOrder()
}

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _inputSearch = MutableStateFlow("")
    val inputSearch: StateFlow<String> = _inputSearch.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>("All")
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _currentSortOrder = MutableStateFlow<SortOrder>(SortOrder.None)
    val currentSortOrder: StateFlow<SortOrder> = _currentSortOrder.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val allProductsFlow: StateFlow<List<Product>> =
        productRepository.getAllProducts()
            .onStart { _isLoading.value = true }
            .onEach { _isLoading.value = false }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val cartItemsFlow: StateFlow<List<CartItem>> =
        cartRepository.getCartItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val categories: StateFlow<List<String>> =
        allProductsFlow
            .map { products ->
                listOf("All") + products.map { it.category }.distinct().sorted()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = listOf("All")
            )

    val filteredProducts: StateFlow<List<Product>> =
        combine(
            allProductsFlow,
            inputSearch.debounce(300).distinctUntilChanged(),
            selectedCategory,
            currentSortOrder
        ) { allItems, query, category, sortOrder ->
            var list = allItems

            if (query.isNotBlank()) {
                list = list.filter { it.name.contains(query, ignoreCase = true) }
            }

            if (!category.isNullOrBlank() && category != "All") {
                list = list.filter { it.category == category }
            }

            list = when (sortOrder) {
                SortOrder.Ascending -> list.sortedBy { it.price }
                SortOrder.Descending -> list.sortedByDescending { it.price }
                SortOrder.None -> list
            }

            list
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getProductQuantityFlow(productId: String): StateFlow<Int> =
        cartItemsFlow
            .map { items -> items.find { it.productId == productId }?.quantity ?: 0 }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )

    fun onInputSearchChanged(query: String) {
        _inputSearch.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onSortOrderChanged(newSortOrder: SortOrder) {
        _currentSortOrder.value = newSortOrder
    }
}
