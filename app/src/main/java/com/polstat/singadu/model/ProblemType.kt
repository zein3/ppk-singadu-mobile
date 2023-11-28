package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class ProblemType(
    val id: Long?,
    val name: String
)