package com.creators.core.di

import com.creators.core.data.local.dao.CalculationDao
import com.creators.core.data.repository.CalculationRepositoryImpl

import com.creators.core.domain.repository.CalculationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    fun provideCalculationRepository(
        calculationDao: CalculationDao // Injected by Hilt
    ): CalculationRepository {
        return CalculationRepositoryImpl(calculationDao)
    }


}