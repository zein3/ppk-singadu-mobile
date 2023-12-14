package com.polstat.singadu.service

import com.polstat.singadu.model.CreateReportForm
import com.polstat.singadu.model.Report
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReportService {
    @POST("/report")
    suspend fun createReport(@Header("Authorization") token: String, @Body report: CreateReportForm)

    @GET("/report")
    suspend fun getAllReports(@Header("Authorization") token: String): List<Report>

    @GET("/user/{userId}/report")
    suspend fun getReportByUser(@Header("Authorization") token: String, @Path("userId") userId: Long): List<Report>

    @DELETE("/report/{reportId}")
    suspend fun deleteReport(@Header("Authorization") token: String, @Path("reportId") reportId: Long)

    @PUT("/report/{reportId}")
    suspend fun updateReport(@Header("Authorization") token: String, @Path("reportId") reportId: Long, @Body report: Report)
}