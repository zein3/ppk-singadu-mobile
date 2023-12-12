package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class Report(
    val id: Long? = null,
    val description: String,
    val solved: Boolean = false,
    val reporter: User? = null,
    val problemType: ProblemType,
    val reportedDate: String,
    val createdOn: String? = null,
    val updatedOn: String? = null
)