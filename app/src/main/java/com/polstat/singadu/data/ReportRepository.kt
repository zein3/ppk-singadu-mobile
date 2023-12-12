package com.polstat.singadu.data

import com.polstat.singadu.model.CreateReportForm
import com.polstat.singadu.service.ReportService

interface ReportRepository {
    suspend fun createReport(token: String, report: CreateReportForm)
}

class NetworkReportRepository(private val reportService: ReportService) : ReportRepository {
    override suspend fun createReport(token: String, report: CreateReportForm) = reportService.createReport("Bearer $token", report)
}