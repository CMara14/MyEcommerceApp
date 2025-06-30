package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.model.Product
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepositoryImpl @Inject constructor() : ProductRepository {

    private val allProducts = listOf(
        Product(
            name = "Smartphone X10",
            price = 799000.0,
            imageUrl = "https://images.unsplash.com/photo-1706300896423-7d08346e8dbb?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica"
        ),
        Product(
            name = "Camiseta Deportiva DryFit",
            price = 25000.0,
            imageUrl = "https://images.unsplash.com/photo-1737094547812-1499f4b700a0?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa"
        ),
        Product(
            name = "Perfume Chanel N° 5",
            price = 350000.0,
            imageUrl = "https://images.unsplash.com/photo-1631701464241-99f7f0ed6f8f?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Beauty"
        ),
        Product(
            name = "Smartwatch Fit Pro",
            price = 65000.0,
            imageUrl = "https://images.unsplash.com/photo-1638798486151-f5247b3c7261?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica"
        ),
        Product(
            name = "Auriculares Bluetooth",
            price = 80500.0,
            imageUrl = "https://images.unsplash.com/photo-1705614055003-d2c0b47d098d?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica"
        ),
        Product(
            name = "Casa tipi",
            price = 200000.0,
            imageUrl = "https://images.unsplash.com/photo-1583512603866-910c8542ba1b?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Mascotas"
        ),
        Product(
            name = "Sofá 3 cuerpos",
            price = 600000.0,
            imageUrl = "https://images.unsplash.com/photo-1698936061086-2bf99c7b9fc5?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Hogar"
        ),
        Product(
            name = "Zapatillas Running Ultra",
            price = 120000.0,
            imageUrl = "https://images.unsplash.com/photo-1606890657878-16393aa45766?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa"
        ),
        Product(
            name = "Teclado Mecánico RGB",
            price = 90000.0,
            imageUrl = "https://images.unsplash.com/photo-1722666729530-a64e3ab266fc?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica"
        ),
        Product(
            name = "Mouse Gamer",
            price = 50000.0,
            imageUrl = "https://images.unsplash.com/photo-1697256936487-34c35c4ededa?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica"
        ),
        Product(
            name = "Gorra Mountain",
            price = 40000.0,
            imageUrl = "https://images.unsplash.com/photo-1560774358-d727658f457c?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa"
        ),
        Product(
            name = "Rascador Gimnasio Casita Para Gatos",
            price = 100000.0,
            imageUrl = "https://images.unsplash.com/photo-1636543459628-12fbfb7478c6?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Mascotas"
        )
    )

    override suspend fun getAllProducts(): List<Product> {
        delay(1500)
        return allProducts.toList()
    }
}
