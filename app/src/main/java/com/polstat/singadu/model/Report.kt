package com.polstat.singadu.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class Report @OptIn(ExperimentalSerializationApi::class) constructor(
    val id: Long? = null,
    val description: String,
    @EncodeDefault val solved: Boolean = false,
    val reporter: User? = null,
    val problemType: ProblemType,
    val reportedDate: String,
    val createdOn: String? = null,
    val updatedOn: String? = null
)