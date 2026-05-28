package ru.itis.android.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey {
    @Serializable data object Home : AppRoute
    @Serializable data object Search : AppRoute
    @Serializable data object Chat : AppRoute
    @Serializable data object Profile : AppRoute
    @Serializable data object ServiceDetail : AppRoute
    @Serializable data object CreateService : AppRoute
    @Serializable data object CreateOrder : AppRoute
    @Serializable data object OrderDetail : AppRoute
    @Serializable data object MasterOrders : AppRoute
    @Serializable data object MasterOrderDetail : AppRoute
    @Serializable data object ClientOrders : AppRoute
    @Serializable data object ClientOrderDetail : AppRoute
    @Serializable data object MasterProfile : AppRoute
    @Serializable data object MasterCompletedOrders : AppRoute
    @Serializable data object ClientCompletedOrders : AppRoute
    @Serializable data object WriteReview : AppRoute
    @Serializable data class ChatRoom(
        val roomId: String,
        val peerName: String,
        val orderId: Long
    ) : AppRoute
}
