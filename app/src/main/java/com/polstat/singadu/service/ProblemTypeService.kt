package com.polstat.singadu.service

import com.polstat.singadu.model.ProblemType
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ProblemTypeService {
    @GET("/problem-type")
    suspend fun getProblemTypes(@Header("Authorization") token: String): List<ProblemType>

    @POST("/problem-type")
    suspend fun createProblemType(@Header("Authorization") token: String, @Body ptype: ProblemType)

    @DELETE("/problem-type/{id}")
    suspend fun deleteProblemType(@Header("Authorization") token: String, @Path("id") id: Long)
}