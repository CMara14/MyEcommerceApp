package com.example.myecommerceapp.ui.screens.productCatalog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myecommerceapp.data.repository.CartRepository
import com.example.myecommerceapp.data.repository.ProductRepository
import com.example.myecommerceapp.domain.GetProductsUseCase
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import com.example.myecommerceapp.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okio.IOException

sealed class SortOrder {
    data object None : SortOrder()
    data object Ascending : SortOrder()
    data object Descending : SortOrder()
}

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val getProductsUseCase: GetProductsUseCase,
    private val cartRepository: CartRepository,
) : ViewModel() {

    var uiState by mutableStateOf<UIState<List<Product>>>(UIState.Loading)
        private set

    private val _inputSearch = MutableStateFlow("")
    val inputSearch: StateFlow<String> = _inputSearch.asStateFlow()

    private val _currentSortOrder = MutableStateFlow<SortOrder>(SortOrder.None)
    val currentSortOrder: StateFlow<SortOrder> = _currentSortOrder.asStateFlow()

    private var hasLoadedInitially = false

    init {
        loadProducts(refreshData = true)
    }

    val allProductsFlow: StateFlow<List<Product>> =
        productRepository.getProductsFlow()
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

    val filteredProducts: StateFlow<List<Product>> = combine(
        allProductsFlow,
        inputSearch.debounce(300).distinctUntilChanged(),
        _currentSortOrder
    ) { allItems, query, sortOrder ->
        var list = if (query.isBlank()) allItems
        else allItems.filter { it.name.contains(query, ignoreCase = true) }

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

    fun onSortOrderChanged(newSortOrder: SortOrder) {
        _currentSortOrder.value = newSortOrder
    }

    fun loadProductsInitial() {
        if (hasLoadedInitially) return
        hasLoadedInitially = true
        loadProducts(refreshData = true, isInitial = true)
    }

    fun loadProducts(
        refreshData: Boolean = false,
        isInitial: Boolean = false
    ) {
        viewModelScope.launch {
            if (isInitial && allProductsFlow.value.isEmpty()) {
                uiState = UIState.Loading
            }

            try {
                val products = getProductsUseCase(refreshData)
                uiState = UIState.Success(products)
            } catch (e: IOException) {
                handleLoadError(isInitial, "Connection failed")
            } catch (e: Exception) {
                handleLoadError(isInitial, "Unexpected server error")
            }
        }
    }

    private fun handleLoadError(isInitial: Boolean, message: String) {
        val cached = allProductsFlow.value
        uiState = if (isInitial && cached.isNotEmpty()) {
            UIState.Success(cached)
        } else {
            UIState.Error(message)
        }
    }
}
