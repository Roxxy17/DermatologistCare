package com.example.dermatologistcare.ui.login.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

// Create a singleton object for your HTTP client
@OptIn(InternalAPI::class)
object NetworkClient {
    private const val BASE_URL = "https://capd-442807.et.r.appspot.com/"

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // Correct way to handle JSON serialization
        }
    }

    suspend fun registerUser(email: String, username: String, password: String): String {
        val response: HttpResponse = client.post("${BASE_URL}register") {
            contentType(ContentType.Application.Json)
            body = TextContent(
                """{
                    "email": "$email",
                    "username": "$username",
                    "password": "$password"
                }""", ContentType.Application.Json
            )
        }

        return response.bodyAsText()
    }


    suspend fun loginUser(email: String, password: String): String {
        val response: HttpResponse = client.post("${BASE_URL}login") {
            contentType(ContentType.Application.Json)
            body = TextContent(
                """{
                    "email": "$email",
                    "password": "$password"
                }""", ContentType.Application.Json
            )
        }

        return response.bodyAsText()
    }

    suspend fun verifyOtp(email: String, otp: String): String {
        val response: HttpResponse = client.post("${BASE_URL}verify-otp") {
            contentType(ContentType.Application.Json)
            body = TextContent(
                """{
                    "email": "$email",
                    "otp": "$otp"
                }""", ContentType.Application.Json
            )
        }

        return response.bodyAsText()
    }
}
