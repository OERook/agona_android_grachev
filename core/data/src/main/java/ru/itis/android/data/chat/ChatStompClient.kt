package ru.itis.android.data.chat

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import ru.itis.android.data.datastore.SessionManager
import ru.itis.android.network.models.NetworkChatMessage
import ru.itis.android.network.models.NetworkSendMessageRequest
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class ChatStompClient @Inject constructor(
    private val sessionManager: SessionManager,
    okHttpClient: OkHttpClient,
    retrofit: Retrofit
) {
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val httpHost: String = retrofit.baseUrl().toString()
        .trimEnd('/')
        .removePrefix("http://").removePrefix("https://")
    private val wsUrl: String = "ws://$httpHost/ws"

    private val client = StompClient(OkHttpWebSocketClient(okHttpClient)) {
        connectionTimeout = 15.seconds
    }

    private val connectionMutex = Mutex()
    @Volatile private var session: StompSession? = null

    private val _incoming = MutableSharedFlow<NetworkChatMessage>(extraBufferCapacity = 64)
    val incoming: Flow<NetworkChatMessage> = _incoming.asSharedFlow()

    private val _connected = MutableSharedFlow<Unit>(extraBufferCapacity = 4)
    val connected: Flow<Unit> = _connected.asSharedFlow()

    private val subscribedRooms = mutableSetOf<String>()
    private val roomJobs = mutableMapOf<String, Job>()

    private var supervisorJob: Job? = null

    suspend fun connect() {
        connectionMutex.withLock {
            if (session != null) return
            supervisorJob?.cancel()
            supervisorJob = scope.launch { runReconnectLoop() }
        }
    }

    suspend fun disconnect() {
        connectionMutex.withLock {
            supervisorJob?.cancel()
            supervisorJob = null
            roomJobs.values.forEach { it.cancel() }
            roomJobs.clear()
            session?.disconnect()
            session = null
        }
    }

    suspend fun subscribeRoom(roomId: String) {
        connectionMutex.withLock {
            subscribedRooms += roomId
        }
        attachRoom(roomId)
    }

    suspend fun send(message: NetworkSendMessageRequest): Boolean {
        val s = session ?: return false
        return try {
            val payload = gson.toJson(message)
            s.send(
                StompSendHeaders(destination = "/app/chat.send"),
                FrameBody.Text(payload)
            )
            true
        } catch (_: Throwable) {
            false
        }
    }

    private suspend fun runReconnectLoop() {
        var attempt = 0
        while (true) {
            try {
                val token = sessionManager.token()
                if (token.isNullOrBlank()) {
                    delay(2.seconds); continue
                }
                val newSession = client.connect(
                    url = wsUrl,
                    customStompConnectHeaders = mapOf("Authorization" to "Bearer $token")
                )
                session = newSession
                attempt = 0
                _connected.tryEmit(Unit)
                val rooms = synchronizedSetSnapshot()
                rooms.forEach { attachRoom(it) }

                awaitCancellation()
            } catch (_: Throwable) {
            } finally {
                session = null
                roomJobs.values.forEach { it.cancel() }
                roomJobs.clear()
            }
            val backoffSeconds = (1L shl attempt.coerceAtMost(5)).coerceAtMost(30L)
            attempt = (attempt + 1).coerceAtMost(5)
            delay(backoffSeconds.seconds)
        }
    }

    private fun synchronizedSetSnapshot(): Set<String> = subscribedRooms.toSet()

    private suspend fun attachRoom(roomId: String) {
        val s = session ?: return
        if (roomJobs.containsKey(roomId)) return
        val job = scope.launch {
            try {
                s.subscribeText("/topic/rooms/$roomId").collect { text ->
                    val msg = runCatching { gson.fromJson(text, NetworkChatMessage::class.java) }.getOrNull()
                    if (msg != null) _incoming.emit(msg)
                }
            } catch (_: Throwable) {
            }
        }
        roomJobs[roomId] = job
    }
}
