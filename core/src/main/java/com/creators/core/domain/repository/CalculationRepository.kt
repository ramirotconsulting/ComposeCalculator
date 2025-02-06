package com.creators.core.domain.repository

interface CalculationRepository {
    suspend fun saveCalculation(calculation: String)
    suspend fun getCalculationHistory(): List<String>
    // Add this for worker backup
    suspend fun saveCalculationToRemote(expression: String, result: String)
}