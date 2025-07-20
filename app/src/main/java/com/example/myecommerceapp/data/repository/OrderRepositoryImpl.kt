package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.local.OrderLocalDataSource
import com.example.myecommerceapp.data.local.entities.OrderEntity
import com.example.myecommerceapp.data.local.entities.OrderItemEntity
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Order
import com.example.myecommerceapp.domain.model.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderLocalDataSource: OrderLocalDataSource
) : OrderRepository {
    override suspend fun createOrder(
        cartItems: List<CartItem>,
        totalAmount: Double,
        totalItems: Int
    ): Long {
        val orderEntity = OrderEntity(
            orderDate = System.currentTimeMillis(),
            totalAmount = totalAmount,
            totalItems = totalItems
        )

        val newOrderId = orderLocalDataSource.insertOrderEntity(orderEntity)

        val orderItemEntities = cartItems.map { cartItem ->
            OrderItemEntity(
                orderId = newOrderId.toInt(),
                productId = cartItem.productId,
                quantity = cartItem.quantity,
                price = cartItem.unitPrice
            )
        }

        orderLocalDataSource.insertOrderItems(orderItemEntities)

        return newOrderId
    }

    override fun getAllOrders(): Flow<List<Order>> {
        return orderLocalDataSource.getAllOrders().map { list ->
            list.map { orderWithItems ->
                Order(
                    id = orderWithItems.order.id,
                    orderDate = orderWithItems.order.orderDate,
                    totalAmount = orderWithItems.order.totalAmount,
                    totalItems = orderWithItems.order.totalItems,
                    items = orderWithItems.orderItems.map { orderItemEntity ->
                        OrderItem(
                            productId = orderItemEntity.productId,
                            quantity = orderItemEntity.quantity,
                            price = orderItemEntity.price
                        )
                    }
                )
            }
        }
    }
}