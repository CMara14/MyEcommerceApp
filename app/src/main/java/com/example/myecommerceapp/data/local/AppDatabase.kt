package com.example.myecommerceapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myecommerceapp.data.local.dao.CartItemDao
import com.example.myecommerceapp.data.local.dao.OrderHistoryDao
import com.example.myecommerceapp.data.local.dao.OrderItemDao
import com.example.myecommerceapp.data.local.dao.ProductDao
import com.example.myecommerceapp.data.local.entities.CartItemEntity
import com.example.myecommerceapp.data.local.entities.OrderEntity
import com.example.myecommerceapp.data.local.entities.OrderItemEntity
import com.example.myecommerceapp.data.local.entities.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun orderHistoryDao(): OrderHistoryDao
}