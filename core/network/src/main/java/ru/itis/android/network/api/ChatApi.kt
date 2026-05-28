package ru.itis.android.network.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.itis.android.network.models.NetworkChatMessage
import ru.itis.android.network.models.NetworkChatRoom
import ru.itis.android.network.models.NetworkOpenRoomRequest
import ru.itis.android.network.models.NetworkSendMessageRequest

interface ChatApi {

    @GET("api/chats")
    suspend fun listRooms(): List<NetworkChatRoom>

    @POST("api/chats/open")
    suspend fun openRoom(@Body body: NetworkOpenRoomRequest): NetworkChatRoom

    @GET("api/chats/{roomId}/messages")
    suspend fun history(
        @Path("roomId") roomId: String,
        @Query("after_id") afterId: Long? = null
    ): List<NetworkChatMessage>

    @POST("api/chats/messages")
    suspend fun sendOverHttp(@Body request: NetworkSendMessageRequest): NetworkChatMessage
}
