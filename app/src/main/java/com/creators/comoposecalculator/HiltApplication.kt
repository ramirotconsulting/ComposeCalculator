package com.creators.comoposecalculator

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager

import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration get() =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Manually initialize WorkManager with the configuration that uses HiltWorkerFactory.
        WorkManager.initialize(this, workManagerConfiguration)
    }
}