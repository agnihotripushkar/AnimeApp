package com.devpush.animeapp.data.di.modules

import com.devpush.animeapp.network.AnimeAPI
import com.devpush.animeapp.network.AnimeAPI_Impl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import io.ktor.client.engine.okhttp.OkHttp

val dataModule = module {
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
        }
    }
    single<AnimeAPI> {
        AnimeAPI_Impl(get())
    }


}