package com.creators.core.domain
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.creators.core.domain.repository.CalculationRepository
import com.creators.core.work.CalculatorWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.work.workDataOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val workManager: WorkManager,
    private val calculationRepository: CalculationRepository
): ViewModel() {

    // Add this state holder
    private val _workStatus = MutableStateFlow<WorkInfo?>(null)
    val workStatus: StateFlow<WorkInfo?> = _workStatus.asStateFlow()

    // Modify the observer
    fun trackWorkStatus(workId: UUID) {
        workManager.getWorkInfoByIdLiveData(workId).observeForever { workInfo ->
            _workStatus.value = workInfo
            when (workInfo?.state) {
                WorkInfo.State.ENQUEUED -> Log.d("WORKER", "Work enqueued")
                WorkInfo.State.RUNNING -> Log.d("WORKER", "Work running")
                WorkInfo.State.SUCCEEDED -> Log.d("WORKER", "Work succeeded")
                WorkInfo.State.FAILED -> Log.d("WORKER", "Work failed")
                WorkInfo.State.BLOCKED -> Log.d("WORKER", "Work blocked")
                WorkInfo.State.CANCELLED -> Log.d("WORKER", "Work cancelled")
                null -> Log.d("WORKER", "Null")
            }
        }
    }
    /*
    Write the string extension in a Util file
    Decouple the viewmodel from the screen ui composable
     */
    var state by mutableStateOf(com.creators.core.domain.CalculatorState())
        private set

    fun onNumberPressed(number: String) {
        state = if (state.shouldResetInput) {
            state.copy(
                currentInput = number,
                shouldResetInput = false
            )
        } else {
            state.copy(
                currentInput = if (state.currentInput == "0") number else state.currentInput + number
            )
        }
    }

    fun onOperatorPressed(operator: String) {
        if (state.currentOperator.isNotEmpty() && !state.shouldResetInput) {
            val result = computeResult(state.firstNumber, state.currentInput, state.currentOperator)
            state = state.copy(
                firstNumber = result,
                currentInput = result,
                currentOperator = operator,
                shouldResetInput = true
            )
        } else {
            state = state.copy(
                firstNumber = state.currentInput,
                currentOperator = operator,
                shouldResetInput = true
            )
        }
    }

    fun onDecimalPressed() {
        if (state.shouldResetInput) {
            state = state.copy(
                currentInput = "0.",
                shouldResetInput = false
            )
        } else {
            if (!state.currentInput.contains('.')) {
                state = state.copy(currentInput = state.currentInput + ".")
            }
        }
    }

    fun onEqualsPressed() {
        if (state.firstNumber.isNotEmpty() && state.currentOperator.isNotEmpty()) {
            val result = computeResult(state.firstNumber, state.currentInput, state.currentOperator)
            state = state.copy(
                currentInput = result,
                firstNumber = "",
                currentOperator = "",
                shouldResetInput = true
            )
        }
    }

    fun onClearPressed() {
        state = com.creators.core.domain.CalculatorState()
    }

    private fun computeResult(
        firstNumberStr: String,
        secondNumberStr: String,
        operator: String
    ): String {
        val first = firstNumberStr.toDoubleOrNull() ?: return "Error"
        val second = secondNumberStr.toDoubleOrNull() ?: return "Error"

        return when (operator) {
            "+" -> (first + second).toString().removeTrailingZeros()
            "-" -> (first - second).toString().removeTrailingZeros()
            "×" -> (first * second).toString().removeTrailingZeros()
            "÷" -> {
                if (second == 0.0) "Error"
                else (first / second).toString().removeTrailingZeros()
            }
            else -> "Error"
        }
    }

    private fun String.removeTrailingZeros(): String {
        return if (contains(".")) {
            replace("\\.0+$", "").replace("0+$", "")
        } else {
            this
        }
    }

    // Update schedule function
    fun scheduleCalculationBackup(calculation: String) {
        val workRequest = OneTimeWorkRequestBuilder<CalculatorWorker>()
            .setInputData(workDataOf(CalculatorWorker.KEY_CALCULATION to calculation))
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).build())
            .build()
//        val inputData = Data.Builder()
//            .putLong(CalculatorWorker.KEY_DURATION_SECONDS, 10L)
//            .build()
//
//        val workRequest = OneTimeWorkRequestBuilder<CalculatorWorker>()
//            .setInputData(inputData)
//            .build()

        workManager.enqueue(workRequest)
        trackWorkStatus(workRequest.id) // Track the new work
    }

    fun saveCalculation(result: String) {
        viewModelScope.launch {
            try {
                // Save to local database
                calculationRepository.saveCalculation(result)

                // Schedule backup
                scheduleCalculationBackup("Backup: $result")

                Log.d("ViewModel", "Calculation saved successfully")
                logCalculations()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error saving calculation", e)
            }
        }
    }

    fun logCalculations() {
        viewModelScope.launch {
            calculationRepository.getCalculationHistory().forEach {
                Log.d("DB", "Calculation: $it")
            }
        }
    }
}