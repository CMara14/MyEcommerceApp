package com.example.myecommerceapp.ui.views.screens.productCatalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.domain.model.Product
import com.example.myecommerceapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class SortOrder {
    object None : SortOrder()
    object Ascending : SortOrder()
    object Descending : SortOrder()
}

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _allProducts.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _inputSearch = MutableStateFlow("")
    val inputSearch: StateFlow<String> = _inputSearch.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _currentSortOrder = MutableStateFlow<SortOrder>(SortOrder.None)
    val currentSortOrder: StateFlow<SortOrder> = _currentSortOrder.asStateFlow()

    val categories: StateFlow<List<String>> = _allProducts
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
            _allProducts,
            _inputSearch.debounce(300L).distinctUntilChanged(),
            _selectedCategory,
            _currentSortOrder
        ) { allItems, query, category, sortOrder ->
            val filteredList = allItems.filter { product ->
                val matchesSearch = if (query.isBlank()) {
                    true
                } else {
                    product.name.contains(query, ignoreCase = true)
                }

                val matchesCategory = if (category == null || category == "All") {
                    true
                } else {
                    product.category == category
                }
                matchesSearch && matchesCategory
            }

            when (sortOrder) {
                is SortOrder.Ascending -> filteredList.sortedBy { it.price }
                is SortOrder.Descending -> filteredList.sortedByDescending { it.price }
                is SortOrder.None -> filteredList
                else -> filteredList
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadProducts()
    }

    private fun loadProducts() {
          viewModelScope.launch {
            _isLoading.value = true
            try {
                val loadedProducts = withContext(Dispatchers.IO) {
                    productRepository.getAllProducts()
                }
                _allProducts.value = loadedProducts
            } catch (e: Exception) {
                println("error al cargar productos")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onInputSearchChanged(query: String) {
        _inputSearch.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = if (_selectedCategory.value == category && category != "All") null else category
        if (category == "All") {
            _selectedCategory.value = "All"
        }
    }
    fun onSortOrderChanged(newSortOrder: SortOrder) {
        _currentSortOrder.value = newSortOrder
    }
}
