package com.creators.core.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.creators.core.domain.repository.CalculationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@HiltWorker
class CalculatorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: CalculationRepository
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_CALCULATION = "calculation"
    }
//companion object {
//    const val KEY_DURATION_SECONDS = "duration_seconds"
//}

    override suspend fun doWork(): Result {
        return try {
            val calculation = inputData.getString(KEY_CALCULATION) ?: "0"
//            calculation?.let {
//                // Parse and save to remote or backup location
//                val parts = it.split("=")
//                if (parts.size == 2) {
            Log.d("calculation", "Saving ${calculation}")
            repository.saveCalculation(calculation)
//                }
//            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }


//override suspend fun doWork(): Result {
//    val durationSeconds = inputData.getLong(KEY_DURATION_SECONDS, 0L)
//    return try {
//        for (i in 1..durationSeconds) {
//            delay(1.seconds)
//        }
//        Result.success()
//    } catch (e: Exception) {
//        Result.failure()
//    }
//}

}