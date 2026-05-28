package ru.itis.android.data.error

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkErrorMapper {

    private const val GENERIC = "Что-то пошло не так. Попробуйте позже"
    private const val NO_NETWORK = "Нет подключения к интернету"
    private const val TIMEOUT = "Сервер не отвечает. Попробуйте позже"
    private const val UNAUTHORIZED = "Сессия истекла, войдите заново"
    private const val FORBIDDEN = "Действие недоступно"
    private const val NOT_FOUND = "Не найдено"
    private const val CONFLICT = "Действие сейчас невозможно"
    private const val SERVER = "Сервер временно недоступен"

    fun fromResponse(response: Response<*>): String =
        fromHttp(response.code(), runCatching { response.errorBody()?.string() }.getOrNull())

    fun fromHttp(code: Int, body: String?): String {
        val serverMessage = body?.takeIf { it.isNotBlank() }?.let { extractMessage(it) }
        if (!serverMessage.isNullOrBlank()) return serverMessage

        return when (code) {
            401 -> UNAUTHORIZED
            403 -> FORBIDDEN
            404 -> NOT_FOUND
            409, 422 -> CONFLICT
            in 500..599 -> SERVER
            else -> GENERIC
        }
    }

    fun fromThrowable(t: Throwable, fallback: String = GENERIC): String = when (t) {
        is UnknownHostException -> NO_NETWORK
        is SocketTimeoutException -> TIMEOUT
        is IOException -> NO_NETWORK
        else -> {
            val msg = t.message
            if (!msg.isNullOrBlank() && looksUserFriendly(msg)) msg else fallback
        }
    }

    private fun extractMessage(body: String): String? = try {
        val json = JSONObject(body)
        json.optString("message").takeIf { it.isNotBlank() }
            ?: json.optString("error").takeIf { it.isNotBlank() }
    } catch (_: Exception) {
        null
    }

    private fun looksUserFriendly(msg: String): Boolean {
        if (msg.length > 200) return false
        if (msg.startsWith("{") || msg.startsWith("[") || msg.startsWith("<")) return false
        val technicalMarkers = listOf(
            "Exception", "java.", "kotlin.", "okhttp", "retrofit",
            "HTTP ", "401 ", "403 ", "404 ", "500 "
        )
        return technicalMarkers.none { it in msg }
    }
}
