package com.polstat.singadu.data

import com.polstat.singadu.model.LoginForm
import com.polstat.singadu.model.LoginResponse
import com.polstat.singadu.model.RegisterForm
import com.polstat.singadu.model.User
import com.polstat.singadu.service.UserService

interface UserRepository {
    suspend fun register(user: RegisterForm): User
    suspend fun login(form: LoginForm): LoginResponse
    suspend fun getProfile(): User
}

class NetworkUserRepository(private val userService: UserService) : UserRepository {
    override suspend fun register(user: RegisterForm): User = userService.register(user)
    override suspend fun login(form: LoginForm): LoginResponse = userService.login(form)
    override suspend fun getProfile(): User = userService.getProfile()
}