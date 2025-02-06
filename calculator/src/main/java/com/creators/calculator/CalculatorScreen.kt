package com.creators.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.WorkInfo
import com.creators.core.domain.CalculatorViewModel


@Preview(showBackground = true)
@Composable
 fun CalculatorScreenPreview(){
    CalculatorScreen()
}

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = hiltViewModel() // If using Hilt
) {
    val state = viewModel.state
    // Collect work status from ViewModel
    val workStatus by viewModel.workStatus.collectAsState()

    // Show status in UI
    workStatus?.let { status ->
        when (status.state) {
            WorkInfo.State.ENQUEUED -> StatusMessage("Calculation queued for backup")
            WorkInfo.State.RUNNING -> StatusMessage("Backing up calculation...")
            WorkInfo.State.SUCCEEDED -> StatusMessage("Backup successful!")
            WorkInfo.State.FAILED -> StatusMessage("Backup failed. Check logs")
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Display
        Text(
            text = state.currentInput,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            textAlign = TextAlign.End
        )

        // Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("C", Modifier.weight(1f)) { viewModel.onClearPressed() }
                CalculatorButton("÷", Modifier.weight(1f)) { viewModel.onOperatorPressed("÷") }
                CalculatorButton("×", Modifier.weight(1f)) { viewModel.onOperatorPressed("×") }
                CalculatorButton("-", Modifier.weight(1f)) { viewModel.onOperatorPressed("-") }
            }

// Second row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("7", Modifier.weight(1f)) { viewModel.onNumberPressed("7") }
                CalculatorButton("8", Modifier.weight(1f)) { viewModel.onNumberPressed("8") }
                CalculatorButton("9", Modifier.weight(1f)) { viewModel.onNumberPressed("9") }
                CalculatorButton("+", Modifier.weight(1f)) { viewModel.onOperatorPressed("+") }
            }

// Third row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("4", Modifier.weight(1f)) { viewModel.onNumberPressed("4") }
                CalculatorButton("5", Modifier.weight(1f)) { viewModel.onNumberPressed("5") }
                CalculatorButton("6", Modifier.weight(1f)) { viewModel.onNumberPressed("6") }
                CalculatorButton("=", Modifier.weight(1f)) { viewModel.onEqualsPressed() }
            }

// Fourth row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton("1", Modifier.weight(1f)) { viewModel.onNumberPressed("1") }
                CalculatorButton("2", Modifier.weight(1f)) { viewModel.onNumberPressed("2") }
                CalculatorButton("3", Modifier.weight(1f)) { viewModel.onNumberPressed("3") }
                Spacer(Modifier.weight(1f))
            }

// Fifth row (0 and decimal)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CalculatorButton(
                    text = "0",
                    modifier = Modifier.weight(2f),
                    aspectRatio = 2f // Width will be twice the height
                ) { viewModel.onNumberPressed("0") }

                CalculatorButton(
                    text = ".",
                    modifier = Modifier.weight(1f)
                ) { viewModel.onDecimalPressed() }

                Spacer(Modifier.weight(1f))
                // In CalculatorScreen composable
                Button(onClick = {
                    viewModel.saveCalculation(state.currentInput)
                    //viewModel.observeWorkStatus()
                }) {
                    Text("Save Calculation")
                }
            }
        }
    }
}

@Composable
private fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f, // Add aspect ratio parameter
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = when (text) {
                "C" -> MaterialTheme.colorScheme.errorContainer
                "+", "-", "×", "÷", "=" -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.secondaryContainer
            },
            contentColor = when (text) {
                "C" -> MaterialTheme.colorScheme.onErrorContainer
                "+", "-", "×", "÷", "=" -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.onSecondaryContainer
            }
        ),
        modifier = modifier
            .aspectRatio(aspectRatio) // Use the parameter here
            .padding(4.dp)
            .sizeIn(minWidth = 64.dp, minHeight = 64.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun StatusMessage(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(16.dp)
    )
}