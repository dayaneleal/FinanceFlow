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
import androidx.compose.ui.text.input.KeyboardType
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

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")


    var newDescription by remember { mutableStateOf(entry.description) }
    var newValue by remember { mutableStateOf(entry.value) }
    var newDate by remember { mutableStateOf(entry.date) }
    var newType by remember { mutableStateOf(entry.type) }


    var showDatePicker by remember { mutableStateOf(false) }


    val initialMillis = try {
        dateFormat.parse(newDate)?.time
    } catch (e: Exception) {
        null
    }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->

                        newDate = dateFormat.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
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
                // Campo Descrição
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Descrição") }
                )

                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = newValue,
                    onValueChange = { newValue = it },
                    label = { Text("Valor") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                    val updatedEntry = entry.copy(
                        description = newDescription,
                        value = newValue,
                        date = newDate,
                        type = newType
                    )
                    onConfirm(updatedEntry)
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