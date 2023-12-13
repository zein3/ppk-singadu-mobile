package com.polstat.singadu.data

import com.polstat.singadu.model.ChangePasswordForm
import com.polstat.singadu.model.LoginForm
import com.polstat.singadu.model.LoginResponse
import com.polstat.singadu.model.RegisterForm
import com.polstat.singadu.model.User
import com.polstat.singadu.service.UserService

interface UserRepository {
    suspend fun register(user: RegisterForm)
    suspend fun login(form: LoginForm): LoginResponse
    suspend fun getProfile(token: String): User
    suspend fun updateProfile(token: String, user: User): User
    suspend fun updatePassword(token: String, form: ChangePasswordForm): User
    suspend fun deleteProfile(token: String)
    suspend fun getAllUsers(token: String): List<User>
    suspend fun deleteUser(token: String, id: Long)
    suspend fun getUserById(token: String, id: Long): User
    suspend fun addRoleToUser(token: String, userId: Long, role: String)
    suspend fun removeRoleFromUser(token: String, userId: Long, role: String)
    suspend fun assignSupervisor(token: String, userId: Long, supervisorId: Long)
    suspend fun dismissSupervisor(token: String, userId: Long)
    suspend fun getAllSupervisee(token: String, userId: Long): List<User>
}

class NetworkUserRepository(private val userService: UserService) : UserRepository {
    override suspend fun register(user: RegisterForm) = userService.register(user)
    override suspend fun login(form: LoginForm): LoginResponse = userService.login(form)
    override suspend fun getProfile(token: String): User = userService.getProfile("Bearer $token")
    override suspend fun updateProfile(token: String, user: User): User = userService.updateProfile("Bearer $token", user)
    override suspend fun updatePassword(token: String, form: ChangePasswordForm): User = userService.updatePassword("Bearer $token", form)
    override suspend fun deleteProfile(token: String) = userService.deleteProfile("Bearer $token")
    override suspend fun getAllUsers(token: String) = userService.getAllUsers("Bearer $token")
    override suspend fun deleteUser(token: String, id: Long) = userService.deleteUser("Bearer $token", id)
    override suspend fun getUserById(token: String, id: Long) = userService.getUserById("Bearer $token", id)
    override suspend fun addRoleToUser(token: String, userId: Long, role: String) = userService.addRoleToUser("Bearer $token", userId, role)
    override suspend fun removeRoleFromUser(token: String, userId: Long, role: String) = userService.removeRoleFromUser("Bearer $token", userId, role)
    override suspend fun assignSupervisor(token: String, userId: Long, supervisorId: Long) = userService.assignSupervisor("Bearer $token", userId, supervisorId)
    override suspend fun dismissSupervisor(token: String, userId: Long) = userService.dismissSupervisor("Bearer $token", userId)
    override suspend fun getAllSupervisee(token: String, userId: Long) = userService.getAllSupervisee("Bearer $token", userId)
}