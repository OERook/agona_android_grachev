package ru.itis.android.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.itis.android.data.chat.ChatStompClient
import ru.itis.android.data.error.NetworkErrorMapper
import ru.itis.android.database.dao.ChatMessageDao
import ru.itis.android.database.dao.ChatRoomDao
import ru.itis.android.database.entity.ChatMessageEntity
import ru.itis.android.database.entity.ChatRoomEntity
import ru.itis.android.model.ChatMessage
import ru.itis.android.model.ChatRoom
import ru.itis.android.model.MessageStatus
import ru.itis.android.network.api.ChatApi
import ru.itis.android.network.models.NetworkChatMessage
import ru.itis.android.network.models.NetworkChatRoom
import ru.itis.android.network.models.NetworkOpenRoomRequest
import ru.itis.android.network.models.NetworkSendMessageRequest
import ru.itis.android.repository.ChatRepository
import java.time.OffsetDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val chatRoomDao: ChatRoomDao,
    private val chatMessageDao: ChatMessageDao,
    private val stompClient: ChatStompClient
) : ChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            stompClient.incoming.collect { msg -> persistIncoming(msg) }
        }
        scope.launch {
            stompClient.connected.collect { flushOutbox() }
        }
    }

    override fun observeRooms(): Flow<List<ChatRoom>> =
        chatRoomDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeMessages(roomId: String): Flow<List<ChatMessage>> =
        chatMessageDao.observeForRoom(roomId).map { list -> list.map { it.toDomain() } }

    override suspend fun openRoomForOrder(orderId: Long): Result<ChatRoom> = safeCall {
        val dto = chatApi.openRoom(NetworkOpenRoomRequest(orderId = orderId))
        val entity = dto.toEntity()
        chatRoomDao.upsert(entity)
        entity.toDomain()
    }

    override suspend fun refreshRooms(): Result<Unit> = safeCall {
        val rooms = chatApi.listRooms()
        chatRoomDao.upsertAll(rooms.map { it.toEntity() })
    }

    override suspend fun refreshMessages(roomId: String): Result<Unit> = safeCall {
        val after = chatMessageDao.maxServerId(roomId).takeIf { it > 0L }
        val fresh = chatApi.history(roomId, after)
        if (fresh.isNotEmpty()) {
            chatMessageDao.upsertAll(fresh.map { it.toSentEntity() })
        }
    }

    override suspend fun sendMessage(roomId: String, text: String): Result<Unit> = safeCall {
        val cmid = UUID.randomUUID().toString()
        val now = OffsetDateTime.now().toString()
        val outgoing = ChatMessageEntity(
            clientMessageId = cmid,
            serverId = null,
            roomId = roomId,
            senderId = "self",
            senderName = "",
            content = text,
            createdAt = now,
            status = MessageStatus.PENDING.name
        )
        chatMessageDao.upsert(outgoing)

        val request = NetworkSendMessageRequest(roomId = roomId, clientMessageId = cmid, content = text)


        val sentViaSocket = stompClient.send(request)
        if (sentViaSocket) {
            chatMessageDao.upsert(outgoing.copy(status = MessageStatus.SENT.name))
            return@safeCall
        }


        runCatching { chatApi.sendOverHttp(request) }
            .onSuccess { dto ->
                chatMessageDao.upsert(
                    outgoing.copy(serverId = dto.id, status = MessageStatus.SENT.name)
                )
            }
    }

    override suspend fun connect() = stompClient.connect()
    override suspend fun disconnect() = stompClient.disconnect()
    override suspend fun subscribeRoom(roomId: String) = stompClient.subscribeRoom(roomId)


    private suspend fun flushOutbox() {
        val pending = chatMessageDao.pendingOutbox()
        pending.forEach { row ->
            val request = NetworkSendMessageRequest(
                roomId = row.roomId,
                clientMessageId = row.clientMessageId,
                content = row.content
            )
            if (stompClient.send(request)) {

                chatMessageDao.upsert(row.copy(status = MessageStatus.SENT.name))
            } else {
                return
            }
        }
    }

    private suspend fun persistIncoming(msg: NetworkChatMessage) {
        val existing = chatMessageDao.findByClientMessageId(msg.clientMessageId)
        val isOwnEcho = existing != null && existing.senderId == "self"
        val merged = if (isOwnEcho) {
            existing!!.copy(
                serverId = msg.id,
                status = MessageStatus.SENT.name,
                createdAt = msg.createdAt
            )
        } else {
            msg.toSentEntity()
        }
        chatMessageDao.upsert(merged)
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (t: Throwable) {
        Result.failure(Exception(NetworkErrorMapper.fromThrowable(t)))
    }


    private fun NetworkChatRoom.toEntity() = ChatRoomEntity(
        id = id,
        orderId = orderId,
        peerId = peerId,
        peerName = peerName,
        lastMessagePreview = lastMessagePreview,
        lastMessageAt = lastMessageAt
    )

    private fun NetworkChatMessage.toSentEntity() = ChatMessageEntity(
        clientMessageId = clientMessageId,
        serverId = id,
        roomId = roomId,
        senderId = senderId,
        senderName = senderName,
        content = content,
        createdAt = createdAt,
        status = MessageStatus.SENT.name
    )

    private fun ChatRoomEntity.toDomain() = ChatRoom(
        id = id,
        orderId = orderId,
        peerId = peerId,
        peerName = peerName,
        lastMessagePreview = lastMessagePreview,
        lastMessageAt = lastMessageAt
    )

    private fun ChatMessageEntity.toDomain() = ChatMessage(
        clientMessageId = clientMessageId,
        serverId = serverId,
        roomId = roomId,
        senderId = senderId,
        senderName = senderName,
        content = content,
        createdAt = createdAt,
        status = runCatching { MessageStatus.valueOf(status) }.getOrDefault(MessageStatus.SENT)
    )
}
