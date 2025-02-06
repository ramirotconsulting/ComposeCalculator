package com.creators.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculations")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val calculation: String,
    val timestamp: Long
)