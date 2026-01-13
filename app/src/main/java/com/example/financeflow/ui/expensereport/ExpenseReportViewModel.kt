package com.example.financeflow.ui.expensereport

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.financeflow.data.AppDatabase
import com.example.financeflow.data.FinancialEntryDao
import com.example.financeflow.domain.FinancialEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ExpenseReportViewModel(private val dao: FinancialEntryDao) : ViewModel() {

    val entriesState: StateFlow<List<FinancialEntry>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val balanceState: StateFlow<String> = entriesState.map { entries ->
        val totalCredits = entries
            .filter { it.type == "Crédito" }
            .sumOf { it.value }

        val totalDebits = entries
            .filter { it.type == "Débito" }
            .sumOf { it.value }

        val balance = totalCredits - totalDebits

        val formattedBalance = (balance / 100.0).let {
            val locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()
            val formatter = NumberFormat.getNumberInstance(locale)
            formatter.minimumFractionDigits = 2
            formatter.maximumFractionDigits = 2

            formatter.format(it)
        }
        formattedBalance
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "0,00"
    )

    fun deleteTransaction(entry: FinancialEntry) {
        viewModelScope.launch {
            dao.delete(entry)
        }
    }

    fun updateTransaction(entry: FinancialEntry) {
        viewModelScope.launch {
            dao.update(entry)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val database = AppDatabase.getDatabase(application)
                ExpenseReportViewModel(database.financialEntryDao())
            }
        }
    }
}