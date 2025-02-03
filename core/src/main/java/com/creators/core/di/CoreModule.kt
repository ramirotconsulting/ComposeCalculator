package com.creators.core.di

import com.creators.core.domain.CalculatorViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    fun provideCalculatorViewModel(): CalculatorViewModel = CalculatorViewModel()
}