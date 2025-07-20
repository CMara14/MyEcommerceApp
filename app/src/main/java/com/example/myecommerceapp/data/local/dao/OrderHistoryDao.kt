package com.example.myecommerceapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.myecommerceapp.data.local.entities.OrderEntity
import com.example.myecommerceapp.data.local.entities.OrderWithItemsAndProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderHistoryDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Transaction
    @Query("SELECT * FROM orders ORDER BY orderDate DESC")
    fun getAllOrdersWithItemsAndProducts(): Flow<List<OrderWithItemsAndProducts>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: Int): OrderEntity?
}