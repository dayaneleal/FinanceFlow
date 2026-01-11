package com.example.financeflow.ui.expensereport

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.financeflow.domain.FinancialEntry

// Alterar quando os dados não estiverem mais mockados
class ExpenseReportViewModel : ViewModel() {
    var entryList by mutableStateOf(emptyList<FinancialEntry>())
    private set

    init {
        getEntries()
    }

    fun getEntries() {
        entryList = listOf(
            FinancialEntry("100.00", "Crédito", "Salário", "2025"),
            FinancialEntry("50.00", "Débito", "Compras", "2025"),
            FinancialEntry("75.00", "Crédito", "Investimentos", "2025"),
        )
    }
}
