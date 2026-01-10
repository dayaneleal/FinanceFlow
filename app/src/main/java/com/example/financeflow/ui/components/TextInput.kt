package com.example.financeflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(
            text = "Descrição",
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            placeholder = {
                Text("Digite uma descrição")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.EditNote,
                    contentDescription = "Selecionar data"
                )
            },
            value = inputValue,
            onValueChange = { newInput -> inputValue = newInput },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}