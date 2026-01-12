package com.example.financeflow.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = "Descrição",
            modifier = Modifier.padding(bottom = 4.dp),
            style = MaterialTheme.typography.titleMedium,

            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
        OutlinedTextField(
            placeholder = {
                Text("Digite uma descrição")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.EditNote,
                    contentDescription = "Ícone de edição"
                )
            },
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),


            isError = isError,


            supportingText = {
                if (isError) {
                    Text(text = "Campo obrigatório", color = MaterialTheme.colorScheme.error)
                }
            }
        )
    }
}