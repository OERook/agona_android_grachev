package ru.itis.android.backend

import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import java.lang.Math.random

@Serializable
data class RegisterRequest(
    val phone: String,
    val password: String,
    val email: String,
    val fullName: String,
    val role: String,
    val city: String,
    val about: String? = null,
    val experienceYears: Int? = null,
    val categories: List<String>? = null
)

@Serializable
data class NetworkUser(
    val id: String,
    val phone: String,
    val email: String,
    val fullName: String,
    val role: String,
    val avatarUrl: String? = null,
    val about: String? = null,
    val experienceYears: Int? = null
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val user: NetworkUser
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Сервер Reparo запущен и готов к работе!")
        }

        route("/auth") {
            post("/register") {
                try {
                    val request = call.receive<RegisterRequest>()
                    println(">>> Получен запрос на регистрацию: ${request.fullName} (${request.role})")

                    val response = AuthResponse(
                        accessToken = "dummy_token_${System.currentTimeMillis()}",
                        user = NetworkUser(
                            id = "user_${(100..999).random()}",
                            phone = request.phone,
                            email = request.email,
                            fullName = request.fullName,
                            role = request.role,
                            about = request.about ?: "Я новый пользователь сервиса Reparo!",
                            experienceYears = request.experienceYears ?: 0
                        )
                    )

                    call.respond(response)
                } catch (e: Exception) {
                    println("Ошибка при регистрации: ${e.message}")
                    call.respondText("Ошибка сервера", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}