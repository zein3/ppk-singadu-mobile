package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordForm(
    val oldPassword: String,
    val newPassword: String
)