package com.polstat.singadu.service

import com.polstat.singadu.model.User
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/register")
    suspend fun register(@Body user: User): User

}