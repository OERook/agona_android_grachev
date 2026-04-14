package ru.itis.android.backend

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode // Добавлен нужный импорт для статусов
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

@Serializable
data class LoginRequest(
    val phone: String,
    val password: String
)

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

        val usersDatabase = mutableMapOf<String, Pair<String, NetworkUser>>()

        route("/auth") {
            post("/register") {
                try {
                    val request = call.receive<RegisterRequest>()

                    if (usersDatabase.containsKey(request.phone)) {
                        call.respondText("Пользователь с таким телефоном уже существует!", status = HttpStatusCode.Conflict)
                        return@post
                    }

                    val networkUser = NetworkUser(
                        id = "user_${(100..999).random()}",
                        phone = request.phone,
                        email = request.email,
                        fullName = request.fullName,
                        role = request.role,
                        about = request.about ?: "Я новый пользователь сервиса Reparo!",
                        experienceYears = request.experienceYears ?: 0
                    )

                    usersDatabase[request.phone] = Pair(request.password, networkUser)

                    val response = AuthResponse(
                        accessToken = "token_${request.phone}_${System.currentTimeMillis()}",
                        user = networkUser
                    )
                    call.respond(response)
                } catch (e: Exception) {
                    call.respondText("Ошибка сервера", status = HttpStatusCode.InternalServerError)
                }
            }

            post("/login") {
                try {
                    val request = call.receive<LoginRequest>()

                    val savedData = usersDatabase[request.phone]

                    if (savedData != null && savedData.first == request.password) {
                        val response = AuthResponse(
                            accessToken = "token_${request.phone}_${System.currentTimeMillis()}",
                            user = savedData.second
                        )
                        call.respond(response)
                    } else {
                        call.respondText(
                            "Неверный телефон или пароль",
                            status = HttpStatusCode.Unauthorized
                        )
                    }
                } catch (e: Exception) {
                    call.respondText("Ошибка сервера", status = HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}