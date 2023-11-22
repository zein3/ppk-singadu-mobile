package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginForm(
    val email: String,
    val password: String
)