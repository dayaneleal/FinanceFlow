package com.example.financeflow.ui.expensereport.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financeflow.domain.FinancialEntry
import com.example.financeflow.ui.expensereport.EditDialogState
import com.example.financeflow.ui.main.components.valueinput.CurrencyVisualTransformation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDialog(
    state: EditDialogState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDescriptionChange: (String) -> Unit,
    onRawValueChange: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onTypeChange: (String) -> Unit,
    onShowDatePicker: (Boolean) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val initialMillis = runCatching { dateFormat.parse(state.date)?.time }.getOrNull()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { onShowDatePicker(false) },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { onShowDatePicker(false) }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Transação") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Descrição") },
                    isError = state.isDescriptionError,
                    supportingText = {
                        if (state.isDescriptionError) {
                            Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.rawValue,
                    visualTransformation = CurrencyVisualTransformation(),
                    onValueChange = onRawValueChange,
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.isValueError,
                    supportingText = {
                        if (state.isValueError) {
                            Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.date,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Data") },
                    trailingIcon = {
                        IconButton(onClick = { onShowDatePicker(true) }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Selecionar data"
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Tipo de Transação:", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = state.type == "Crédito",
                        onClick = { onTypeChange("Crédito") }
                    )
                    Text("Crédito", modifier = Modifier.padding(end = 16.dp))

                    RadioButton(
                        selected = state.type == "Débito",
                        onClick = { onTypeChange("Débito") }
                    )
                    Text("Débito")
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Desistir")
            }
        }
    )
}
