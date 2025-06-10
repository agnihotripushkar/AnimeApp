package com.devpush.animeapp.features.auth.data.repository

import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay

class AuthRepositoryImpl(val client: HttpClient) : AuthRepository {
    override suspend fun loginCall() {
        delay(2000)
    }
}