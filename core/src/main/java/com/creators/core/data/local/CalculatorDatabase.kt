package com.creators.core.data.local

import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.creators.core.data.local.dao.CalculationDao
import com.creators.core.data.local.entity.CalculationEntity

@Database(
    entities = [CalculationEntity::class],
    version = 1
)
//@TypeConverters(Converters::class)
abstract class CalculatorDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao
}
