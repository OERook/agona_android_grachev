package ru.itis.android.data.repository

import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.model.CreateOrderRequest
import ru.itis.android.model.Order
import ru.itis.android.model.OrderStatus
import ru.itis.android.network.api.OrdersApi
import ru.itis.android.network.models.NetworkCreateOrderRequest
import ru.itis.android.network.models.NetworkOrder
import ru.itis.android.repository.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val ordersApi: OrdersApi
) : OrdersRepository {

    override suspend fun createOrder(request: CreateOrderRequest): Result<Order> = safeCall {
        val response = ordersApi.createOrder(
            NetworkCreateOrderRequest(
                serviceId = request.serviceId,
                address = request.address,
                scheduledDate = request.scheduledDate,
                scheduledTime = request.scheduledTime,
                description = request.description,
                estimatedPrice = request.estimatedPrice
            )
        )
        if (!response.isSuccessful) throw friendly(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw friendly("Сервер не вернул данные")
    }

    override suspend fun getOrderById(id: Long): Result<Order> = safeCall {
        ordersApi.getOrderById(id).toDomain()
    }

    override suspend fun getMyOrders(): Result<List<Order>> = safeCall {
        ordersApi.getMyOrders().map { it.toDomain() }
    }

    override suspend fun updateOrderStatus(orderId: Long, status: OrderStatus): Result<Order> = safeCall {
        val response = ordersApi.updateOrderStatus(orderId, mapOf("status" to status.name))
        if (!response.isSuccessful) throw friendly(NetworkErrorMapper.fromResponse(response))
        response.body()?.toDomain() ?: throw friendly("Сервер не вернул данные")
    }


    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }

    private fun friendly(message: String): Throwable = Exception(message)

    private fun NetworkOrder.toDomain(): Order = Order(
        id = id,
        clientId = clientId,
        masterId = masterId,
        serviceId = serviceId,
        serviceTitle = serviceTitle,
        serviceCategoryId = serviceCategoryId.orEmpty(),
        status = parseStatusOrNull(status) ?: OrderStatus.PENDING,
        address = address,
        scheduledDate = scheduledDate,
        scheduledTime = scheduledTime,
        description = description.orEmpty(),
        estimatedPrice = estimatedPrice,
        finalPrice = finalPrice,
        createdAt = createdAt,
        acceptedAt = acceptedAt,
        startedAt = startedAt,
        completedAt = completedAt,
        cancelledAt = cancelledAt,
        availableActions = availableActions
            ?.mapNotNull(::parseStatusOrNull)
            .orEmpty()
    )

    private fun parseStatusOrNull(raw: String): OrderStatus? = when (raw.uppercase()) {
        "PENDING" -> OrderStatus.PENDING
        "ACCEPTED" -> OrderStatus.ACCEPTED
        "IN_PROGRESS" -> OrderStatus.IN_PROGRESS
        "COMPLETED" -> OrderStatus.COMPLETED
        "CANCELLED" -> OrderStatus.CANCELLED
        "DISPUTED" -> OrderStatus.DISPUTED
        else -> null
    }
}
