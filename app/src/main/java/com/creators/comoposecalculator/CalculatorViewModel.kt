package com.creators.comoposecalculator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {
    /*
    Write the string extension in a Util file
    Decouple the viewmodel from the screen ui composable
     */
    var state by mutableStateOf(CalculatorState())
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
        state = CalculatorState()
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
}