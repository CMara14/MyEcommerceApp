package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.dao.ProductDao
import com.example.myecommerceapp.data.local.entities.ProductEntity
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductsLocalDataSource {

    private val initialProducts = listOf(
        Product(
            id = "1",
            name = "Smartphone X10",
            price = 799000.0,
            imageUrl = "https://images.unsplash.com/photo-1706300896423-7d08346e8dbb?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica",
            description = "Un smartphone de alta gama con cámara avanzada y gran rendimiento.",
            hasDrink = false
        ),
        Product(
            id = "2",
            name = "Camiseta Deportiva DryFit",
            price = 25000.0,
            imageUrl = "https://images.unsplash.com/photo-1737094547812-1499f4b700a0?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa",
            description = "Camiseta transpirable ideal para entrenamientos intensos.",
            hasDrink = false
        ),
        Product(
            id = "3",
            name = "Perfume Chanel N° 5",
            price = 350000.0,
            imageUrl = "https://images.unsplash.com/photo-1631701464241-99f7f0ed6f8f?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Beauty",
            description = "Fragancia icónica con notas florales y amaderadas.",
            hasDrink = false
        ),
        Product(
            id = "4",
            name = "Smartwatch Fit Pro",
            price = 65000.0,
            imageUrl = "https://images.unsplash.com/photo-1638798486151-f5247b3c7261?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica",
            description = "Monitor de actividad con GPS y notificaciones inteligentes.",
            hasDrink = false
        ),
        Product(
            id = "5",
            name = "Auriculares Bluetooth",
            price = 80500.0,
            imageUrl = "https://images.unsplash.com/photo-1705614055003-d2c0b47d098d?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica",
            description = "Sonido de alta fidelidad con cancelación de ruido activa.",
            hasDrink = false
        ),
        Product(
            id = "6",
            name = "Casa tipi",
            price = 200000.0,
            imageUrl = "https://images.unsplash.com/photo-1583512603866-910c8542ba1b?q=80&w=688&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Mascotas",
            description = "Cama en forma de tipi para mascotas, cómoda y decorativa.",
            hasDrink = false
        ),
        Product(
            id = "7",
            name = "Sofá 3 cuerpos",
            price = 600000.0,
            imageUrl = "https://images.unsplash.com/photo-1698936061086-2bf99c7b9fc5?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Hogar",
            description = "Sofá espacioso y confortable para tu sala de estar.",
            hasDrink = false
        ),
        Product(
            id = "8",
            name = "Zapatillas Running Ultra",
            price = 120000.0,
            imageUrl = "https://images.unsplash.com/photo-1606890657878-16393aa45766?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa",
            description = "Zapatillas ligeras y con amortiguación para corredores exigentes.",
            hasDrink = false
        ),
        Product(
            id = "9",
            name = "Teclado Mecánico RGB",
            price = 90000.0,
            imageUrl = "https://images.unsplash.com/photo-1722666729530-a64e3ab266fc?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica",
            description = "Teclado con switches mecánicos y retroiluminación RGB personalizable.",
            hasDrink = false
        ),
        Product(
            id = "10",
            name = "Mouse Gamer",
            price = 50000.0,
            imageUrl = "https://images.unsplash.com/photo-1697256936487-34c35c4ededa?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Electrónica",
            description = "Mouse de alta precisión con botones programables para gaming.",
            hasDrink = false
        ),
        Product(
            id = "11",
            name = "Gorra Mountain",
            price = 40000.0,
            imageUrl = "https://images.unsplash.com/photo-1560774358-d727658f457c?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Ropa",
            description = "Gorra estilo casual para uso diario o actividades al aire libre.",
            hasDrink = false
        ),
        Product(
            id = "12",
            name = "Rascador Gimnasio Casita Para Gatos",
            price = 100000.0,
            imageUrl = "https://images.unsplash.com/photo-1636543459628-12fbfb7478c6?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            category = "Mascotas",
            description = "Centro de actividades para mantener a tu gato entretenido y activo.",
            hasDrink = false
        )
    )

    private var isDbInitialized = false

    override fun getProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()
    override suspend fun insertProducts(products: List<ProductEntity>) =
        productDao.insertProducts(products)

    override suspend fun clearProducts() = productDao.deleteAllProducts()
    override suspend fun getProductCount(): Int = productDao.getProductCount()

    override suspend fun initializeProductsIfEmpty() {
        if (!isDbInitialized) {
            val productCount = productDao.getProductCount()
            if (productCount == 0) {
                val productEntities = initialProducts.map { it.toEntity() }
                productDao.insertProducts(productEntities)
            }
            isDbInitialized = true
        }
    }

    override suspend fun getProductById(productId: String): ProductEntity? {
        try {
            val entity = productDao.getProductById(productId)
            return entity
        } catch (e: Exception) {
            return null
        }
    }

    private fun Product.toEntity(): ProductEntity {
        return ProductEntity(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            price = this.price,
            hasDrink = this.hasDrink,
            category = this.category
        )
    }
}