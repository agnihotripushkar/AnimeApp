package com.devpush.animeapp.features.auth.domain.repository

interface AuthRepository {

    suspend fun loginCall()
}