package com.polstat.singadu.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateReportForm(
    val description: String,
    val solved: Boolean,
    val problemType: ProblemType,
    val reportedDate: String
)