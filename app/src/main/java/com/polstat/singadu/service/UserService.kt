package com.polstat.singadu.service

import com.polstat.singadu.model.LoginForm
import com.polstat.singadu.model.LoginResponse
import com.polstat.singadu.model.RegisterForm
import com.polstat.singadu.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("/register")
    suspend fun register(@Body user: RegisterForm)

    @POST("/login")
    suspend fun login(@Body form: LoginForm): LoginResponse

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): User

}