package com.example.financeflow.ui.expensereport.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun EditTransactionDialog(
    entry: FinancialEntry,
    onDismiss: () -> Unit,
    onConfirm: (FinancialEntry) -> Unit
) {

    var newDescription by remember { mutableStateOf(entry.description) }
    var newValue by remember { mutableStateOf(entry.value) }
    var newDate by remember { mutableStateOf(entry.date) }
    var newType by remember { mutableStateOf(entry.type) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Editar Transação") },
        text = {
            Column {
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
                    onValueChange = { newDate = it },
                    label = { Text("Data") },
                    placeholder = { Text("DD/MM/AAAA") }
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