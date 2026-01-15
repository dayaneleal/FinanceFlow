package com.example.financeflow.ui.expensereport

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financeflow.ui.expensereport.components.EditTransactionDialog
import com.example.financeflow.ui.expensereport.components.EntryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    navController: NavController,
    viewModel: ExpenseReportViewModel = viewModel(factory = ExpenseReportViewModel.Factory)
) {
    val expenses by viewModel.entriesState.collectAsState()

    val balance by viewModel.balanceState.collectAsState()

    val editDialogState by viewModel.editDialogState.collectAsState()

    val entryToDelete by viewModel.entryToDelete.collectAsState()

    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = viewModel::onDeleteDialogDismiss,
            title = { Text(text = "Excluir transação?") },
            text = { Text("Deseja excluir '${entryToDelete?.description}'?") },
            confirmButton = {
                TextButton(onClick = viewModel::onDeleteConfirm) { Text("Sim, excluir") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDeleteDialogDismiss) { Text("Cancelar") }
            }
        )
    }


    editDialogState.entryToEdit?.let { entry ->
        EditTransactionDialog(
            state = editDialogState,
            onDismiss = viewModel::onEditDialogDismiss,
            onConfirm = viewModel::onEditConfirm,
            onDescriptionChange = viewModel::onEditDescriptionChange,
            onRawValueChange = viewModel::onEditRawValueChange,
            onDateSelected = viewModel::onEditDateChange,
            onTypeChange = viewModel::onEditTypeChange,
            onShowDatePicker = viewModel::onShowDatePicker
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Extrato") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(16.dp)
                ) {
                    Text("Saldo Atual")
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("R$ ", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            balance,
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            items(expenses) { entry ->
                EntryItem(
                    entry = entry,
                    onDeleteClick = { viewModel.onDeleteEntryClick(entry) },
                    onEditClick = { viewModel.onEditEntryClick(entry) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}