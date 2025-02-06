package com.creators.core.data.repository

import com.creators.core.data.local.dao.CalculationDao
import com.creators.core.data.local.entity.CalculationEntity
import com.creators.core.domain.repository.CalculationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationRepositoryImpl @Inject constructor(
    private val calculationDao: CalculationDao
) : CalculationRepository {

    override suspend fun saveCalculation(calculation: String) {
        calculationDao.insertCalculation(
            CalculationEntity(
                calculation = calculation,
                timestamp = System.currentTimeMillis()
            )
        )
    }
    override suspend fun saveCalculationToRemote(expression: String, result: String) {
        // Implement your remote backup logic here
    }

    override suspend fun getCalculationHistory(): List<String> {
        return calculationDao.getCalculations().map { it.calculation }
    }
}