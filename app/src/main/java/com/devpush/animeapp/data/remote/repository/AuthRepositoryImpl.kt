package com.devpush.animeapp.data.remote.repository

import com.devpush.animeapp.domian.repository.AuthRepository
import com.devpush.animeapp.network.KitsuApi

class AuthRepositoryImpl(private val api: KitsuApi) : AuthRepository {
    override suspend fun loginCall() {
        api.loginCall()
    }
}