package com.polstat.singadu.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.polstat.singadu.service.UserService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val userRepository: UserRepository
}

class DefaultAppContainer() : AppContainer {
    private val baseUrl = "http://localhost:8080"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(retrofitService)
    }
}