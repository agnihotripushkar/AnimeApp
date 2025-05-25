package com.devpush.animeapp.network

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception,val message: String?=null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}