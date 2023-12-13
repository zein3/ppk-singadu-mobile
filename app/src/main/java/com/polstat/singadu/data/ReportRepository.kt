package com.polstat.singadu.data

import com.polstat.singadu.model.CreateReportForm
import com.polstat.singadu.model.Report
import com.polstat.singadu.service.ReportService

interface ReportRepository {
    suspend fun createReport(token: String, report: CreateReportForm)
    suspend fun getAllReports(token: String): List<Report>
    suspend fun getReportsByUser(token: String, userId: Long): List<Report>
}

class NetworkReportRepository(private val reportService: ReportService) : ReportRepository {
    override suspend fun createReport(token: String, report: CreateReportForm) = reportService.createReport("Bearer $token", report)
    override suspend fun getAllReports(token: String) = reportService.getAllReports("Bearer $token")
    override suspend fun getReportsByUser(token: String, userId: Long) = reportService.getReportByUser("Bearer $token", userId)
}