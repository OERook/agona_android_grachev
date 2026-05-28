package ru.itis.android.repository

import ru.itis.android.model.CreateOrderRequest
import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus

interface OrdersRepository {
    suspend fun createOrder(request: CreateOrderRequest): Result<Order>

    suspend fun getOrderById(id: Long): Result<Order>

    suspend fun getMyOrders(): Result<List<Order>>

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus): Result<Order>
}