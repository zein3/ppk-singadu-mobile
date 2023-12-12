package com.polstat.singadu.service

import com.polstat.singadu.model.CreateReportForm
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReportService {
    @POST("/report")
    suspend fun createReport(@Header("Authorization") token: String, @Body report: CreateReportForm)
}