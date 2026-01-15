package com.example.financeflow.ui.expensereport.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeflow.domain.FinancialEntry
import com.example.financeflow.ui.theme.success
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EntryItem(
    entry: FinancialEntry,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val formattedValue = (entry.value / 100.0).let {
        val locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()
        val formatter = NumberFormat.getNumberInstance(locale)
        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2

        formatter.format(it)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${entry.date} • ${entry.type}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = if (entry.type == "Crédito") "+ R$ $formattedValue" else "- R$ $formattedValue",
                style = MaterialTheme.typography.titleMedium,
                color = if (entry.type == "Crédito") MaterialTheme.colorScheme.success else MaterialTheme.colorScheme.error,)
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color.Gray
                )
            }
        }
    }
}