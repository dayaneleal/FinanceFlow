package com.example.financeflow.ui.expensereport

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financeflow.domain.FinancialEntry
import com.example.financeflow.ui.expensereport.components.EditTransactionDialog
import com.example.financeflow.ui.expensereport.components.EntryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    navController: NavController,
    viewModel: ExpenseReportViewModel = viewModel(factory = ExpenseReportViewModel.Factory)
) {
    val expenses by viewModel.uiState.collectAsState()


    var entryToDelete by remember { mutableStateOf<FinancialEntry?>(null) }


    var entryToEdit by remember { mutableStateOf<FinancialEntry?>(null) }


    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text(text = "Excluir transação?") },
            text = { Text("Deseja excluir '${entryToDelete?.description}'?") },
            confirmButton = {
                TextButton(onClick = {
                    entryToDelete?.let { viewModel.deleteTransaction(it) }
                    entryToDelete = null
                }) { Text("Sim, excluir") }
            },
            dismissButton = {
                TextButton(onClick = { entryToDelete = null }) { Text("Cancelar") }
            }
        )
    }


    entryToEdit?.let { entry ->
        EditTransactionDialog(
            entry = entry,
            onDismiss = { entryToEdit = null },
            onConfirm = { updatedEntry ->
                viewModel.updateTransaction(updatedEntry)
                entryToEdit = null
            }
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
            items(expenses) { entry ->
                EntryItem(
                    entry = entry,
                    onDeleteClick = { entryToDelete = entry },
                    onEditClick = { entryToEdit = entry }
                )
            }
        }
    }
}