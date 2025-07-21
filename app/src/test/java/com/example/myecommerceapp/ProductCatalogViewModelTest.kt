package com.example.myecommerceapp

import com.example.myecommerceapp.data.repository.CartRepository
import com.example.myecommerceapp.data.repository.ProductRepository
import com.example.myecommerceapp.domain.GetProductsUseCase
import com.example.myecommerceapp.domain.model.Product
import com.example.myecommerceapp.ui.UIState
import com.example.myecommerceapp.ui.screens.productCatalog.ProductCatalogViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException


@OptIn(ExperimentalCoroutinesApi::class)
class ProductCatalogViewModelTest {
    private lateinit var viewModel: ProductCatalogViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var getProductsUseCase: GetProductsUseCase

    private val testDispatcher = StandardTestDispatcher()

    private val fakeProducts = listOf(
        Product(id = "1", name = "Laptop", price = 1500.0, imageUrl = "", description = "", hasDrink = false),
        Product(id = "2", name = "Mouse", price = 20.0, imageUrl = "", description = "", hasDrink = false),
        Product(id = "3", name = "Keyboard", price = 50.0, imageUrl = "", description = "", hasDrink = false)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        productRepository = mockk()
        cartRepository = mockk()
        getProductsUseCase = mockk()

        every { productRepository.getProductsFlow() } returns MutableStateFlow(fakeProducts)
        every { cartRepository.getCartItems() } returns MutableStateFlow(emptyList())

        viewModel = ProductCatalogViewModel(productRepository, getProductsUseCase, cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts updates uiState with Success when there are products`() = runTest {
        coEvery { getProductsUseCase(any()) } returns fakeProducts

        viewModel.loadProducts(refreshData = true)
        advanceUntilIdle()

        assertTrue(viewModel.uiState is UIState.Success)
        val success = viewModel.uiState as UIState.Success
        assertEquals(3, success.data.size)
        coVerify { getProductsUseCase(true) }
    }

    @Test
    fun `loadProducts sets uiState to Error when there is IOException`() = runTest {
        coEvery { getProductsUseCase(any()) } throws IOException("Network error")

        viewModel.loadProducts(refreshData = true)
        advanceUntilIdle()

        assertTrue(viewModel.uiState is UIState.Error)
        val error = viewModel.uiState as UIState.Error
        assertEquals("Connection failed", error.message)
    }
}