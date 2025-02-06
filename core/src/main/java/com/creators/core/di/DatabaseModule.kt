package com.creators.core.di

import android.content.Context
import androidx.room.Room
import com.creators.core.data.local.CalculatorDatabase
import com.creators.core.data.local.dao.CalculationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CalculatorDatabase {
        return Room.databaseBuilder(
            context,
            CalculatorDatabase::class.java,
            "calculator_db"
        ).build()
    }

    @Provides
    fun provideCalculationDao(database: CalculatorDatabase): CalculationDao {
        return database.calculationDao()
    }
}