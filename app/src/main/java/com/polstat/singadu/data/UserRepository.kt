package com.polstat.singadu.data

import com.polstat.singadu.model.User
import com.polstat.singadu.service.UserService

interface UserRepository {
    suspend fun register(user: User): User
}

class NetworkUserRepository(private val userService: UserService) : UserRepository {
    override suspend fun register(user: User): User = userService.register(user)
}