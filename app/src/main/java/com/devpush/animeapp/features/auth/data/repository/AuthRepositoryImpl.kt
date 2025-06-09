package com.devpush.animeapp.features.auth.data.repository

import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import com.devpush.animeapp.core.network.KitsuApi
import io.ktor.client.HttpClient

class AuthRepositoryImpl(val client: HttpClient) : AuthRepository {
    override suspend fun loginCall() {
        // Implementation of login call
    }
}