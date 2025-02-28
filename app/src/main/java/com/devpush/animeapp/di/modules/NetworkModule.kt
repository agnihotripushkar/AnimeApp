package com.devpush.animeapp.di.modules

import com.devpush.animeapp.BuildConfig
import com.devpush.animeapp.network.KitsuApi
import com.devpush.animeapp.network.KitsuApi.Companion.baseUrl
import com.devpush.animeapp.network.KitsuApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.android.AndroidEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


val networkKoinModule = module {
    single { provideKtorClient() }
    single { provideApiService(get()) }
}

private fun provideKtorClient(): HttpClient {

    val ktorClient = HttpClient(Android) {
        enableLogging(this)
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            url(baseUrl)
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, "application/vnd.api+json")
        }
    }
    return ktorClient
}

private fun enableLogging(httpClientConfig: HttpClientConfig<AndroidEngineConfig>) {
    httpClientConfig.install(Logging) {
        logger = object : Logger {

            override fun log(message: String) {
                println("KtorClient: $message")
            }
        }

        level = if (BuildConfig.DEBUG) {
            LogLevel.ALL
        } else {
            LogLevel.NONE
        }
    }

}

private fun provideApiService(cilent: HttpClient): KitsuApi = KitsuApiImpl(cilent)