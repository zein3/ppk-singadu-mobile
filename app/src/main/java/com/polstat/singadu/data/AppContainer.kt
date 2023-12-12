package com.polstat.singadu.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.polstat.singadu.service.ProblemTypeService
import com.polstat.singadu.service.ReportService
import com.polstat.singadu.service.UserService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val userRepository: UserRepository
    val problemTypeRepository: ProblemTypeRepository
    val reportRepository: ReportRepository
}

class DefaultAppContainer() : AppContainer {
    private val baseUrl = "http://10.0.2.2:8080"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build())
        .build()

    private val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
    private val problemTypeService: ProblemTypeService by lazy {
        retrofit.create(ProblemTypeService::class.java)
    }
    private val reportService: ReportService by lazy {
        retrofit.create(ReportService::class.java)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(userService)
    }

    override val problemTypeRepository: ProblemTypeRepository by lazy {
        NetworkProblemTypeRepository(problemTypeService)
    }

    override val reportRepository: ReportRepository by lazy {
        NetworkReportRepository(reportService)
    }
}