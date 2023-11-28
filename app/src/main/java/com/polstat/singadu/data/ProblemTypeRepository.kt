package com.polstat.singadu.data

import com.polstat.singadu.model.ProblemType
import com.polstat.singadu.service.ProblemTypeService

interface ProblemTypeRepository {
    suspend fun getProblemTypes(token: String): List<ProblemType>
    suspend fun createProblemType(token: String, ptype: ProblemType)
    suspend fun deleteProblemType(token: String, id: Long)
}

class NetworkProblemTypeRepository(private val problemTypeService: ProblemTypeService) : ProblemTypeRepository {
    override suspend fun getProblemTypes(token: String): List<ProblemType> = problemTypeService.getProblemTypes("Bearer $token")
    override suspend fun createProblemType(token: String, ptype: ProblemType) = problemTypeService.createProblemType("Bearer $token", ptype)
    override suspend fun deleteProblemType(token: String, id: Long) = problemTypeService.deleteProblemType("Bearer $token", id)
}