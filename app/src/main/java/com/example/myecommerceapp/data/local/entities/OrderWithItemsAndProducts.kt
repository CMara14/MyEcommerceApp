package com.example.myecommerceapp.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class OrderWithItemsAndProducts(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId",
        entity = OrderItemEntity::class
    )
    val orderItems: List<OrderItemEntity>
)