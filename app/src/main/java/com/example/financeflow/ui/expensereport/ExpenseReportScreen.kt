package com.example.financeflow.ui.expensereport

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog // Importante
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // Importante
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf // Importante
import androidx.compose.runtime.remember // Importante
import androidx.compose.runtime.setValue // Importante
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financeflow.domain.FinancialEntry
import com.example.financeflow.ui.expensereport.components.EntryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    navController: NavController,
    viewModel: ExpenseReportViewModel = viewModel(factory = ExpenseReportViewModel.Factory)
) {
    val expenses by viewModel.uiState.collectAsState()


    var entryToDelete by remember { mutableStateOf<FinancialEntry?>(null) }


    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text(text = "Excluir transação?") },
            text = { Text("Você tem certeza que deseja excluir '${entryToDelete?.description}'? Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {

                        entryToDelete?.let { viewModel.deleteTransaction(it) }
                        entryToDelete = null
                    }
                ) {
                    Text("Sim, excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { entryToDelete = null }
                ) {
                    Text("Cancelar")
                }
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
                    onDeleteClick = {
                        entryToDelete = entry
                    }
                )
            }
        }
    }
}