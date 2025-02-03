package com.creators.comoposecalculator

data class CalculatorState(
    val currentInput: String = "0",
    val firstNumber: String = "",
    val currentOperator: String = "",
    val shouldResetInput: Boolean = false
)