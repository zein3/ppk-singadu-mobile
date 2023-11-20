package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterForm(
    val name: String,
    val email: String,
    val password: String
)