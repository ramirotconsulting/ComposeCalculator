package com.creators.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.creators.core.data.local.entity.CalculationEntity

@Dao
interface CalculationDao {
    @Insert
    suspend fun insertCalculation(calculation: CalculationEntity)

    @Query("SELECT * FROM calculations ORDER BY timestamp DESC")
    suspend fun getCalculations(): List<CalculationEntity>
}