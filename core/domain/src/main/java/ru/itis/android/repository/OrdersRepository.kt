package ru.itis.android.repository

import ru.itis.android.model.CreateOrderRequest
import ru.itis.android.model.Order

interface OrdersRepository {
    suspend fun createOrder(request: CreateOrderRequest): Result<Order>

    suspend fun getMyOrders(): Result<List<Order>>
}