package com.kevinfreyap.network.client

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.takeFrom
import com.kevinfreyap.network.BuildConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun provideKtorClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Resources)

        install(WebSockets) {
            pingInterval = 20.seconds
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }

        defaultRequest {
            url {
                takeFrom(BuildConfig.BASE_URL)
                protocol = URLProtocol.HTTPS
            }
        }
    }
}