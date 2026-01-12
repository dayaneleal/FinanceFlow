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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.financeflow.domain.FinancialEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDialog(
    entry: FinancialEntry,
    onDismiss: () -> Unit,
    onConfirm: (FinancialEntry) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }


    val initialRawString = entry.value.toDoubleOrNull()?.let { doubleVal ->
        (doubleVal * 100).toLong().toString()
    } ?: ""


    var newDescription by remember { mutableStateOf(entry.description) }
    var rawValue by remember { mutableStateOf(initialRawString) }
    var newDate by remember { mutableStateOf(entry.date) }
    var newType by remember { mutableStateOf(entry.type) }


    var isDescriptionError by remember { mutableStateOf(false) }
    var isValueError by remember { mutableStateOf(false) }


    var showDatePicker by remember { mutableStateOf(false) }


    val initialMillis = runCatching {
        dateFormat.parse(newDate)?.time
    }.getOrNull()

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)


    fun formatToCurrency(raw: String): String {
        if (raw.isEmpty()) return ""
        val longVal = raw.toLongOrNull() ?: 0L


        return String.format(Locale.getDefault(), "%d,%02d", longVal / 100, longVal % 100)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        newDate = dateFormat.format(Date(millis))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
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
                    value = newDescription,
                    onValueChange = {
                        newDescription = it
                        isDescriptionError = false
                    },
                    label = { Text("Descrição") },
                    isError = isDescriptionError,
                    supportingText = {
                        if (isDescriptionError) {
                            Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = TextFieldValue(
                        text = formatToCurrency(rawValue),
                        selection = TextRange(formatToCurrency(rawValue).length)
                    ),
                    onValueChange = { tfv ->
                        val newDigits = tfv.text.filter { it.isDigit() }
                        if (newDigits.length <= 18) {
                            rawValue = newDigits
                            isValueError = false
                        }
                    },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    isError = isValueError,
                    supportingText = {
                        if (isValueError) {
                            Text("Campo obrigatório", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = newDate,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Data") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
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
                        selected = newType == "Crédito",
                        onClick = { newType = "Crédito" }
                    )
                    Text("Crédito", modifier = Modifier.padding(end = 16.dp))

                    RadioButton(
                        selected = newType == "Débito",
                        onClick = { newType = "Débito" }
                    )
                    Text("Débito")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val descriptionValid = newDescription.isNotBlank()
                    val valueValid = rawValue.isNotBlank() && (rawValue.toLongOrNull() ?: 0) > 0

                    if (!descriptionValid) isDescriptionError = true
                    if (!valueValid) isValueError = true

                    if (descriptionValid && valueValid) {
                        val finalValue = (rawValue.toLong() / 100.0).toString()

                        val updatedEntry = entry.copy(
                            description = newDescription,
                            value = finalValue,
                            date = newDate,
                            type = newType
                        )
                        onConfirm(updatedEntry)
                    }
                }
            ) {
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