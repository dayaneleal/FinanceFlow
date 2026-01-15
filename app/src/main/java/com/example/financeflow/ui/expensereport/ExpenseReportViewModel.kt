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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    private val _editDialogState = MutableStateFlow(EditDialogState())
    val editDialogState = _editDialogState.asStateFlow()

    private val _entryToDelete = MutableStateFlow<FinancialEntry?>(null)
    val entryToDelete: StateFlow<FinancialEntry?> = _entryToDelete.asStateFlow()

    fun onDeleteEntryClick(entry: FinancialEntry) {
        _entryToDelete.value = entry
    }

    fun onDeleteDialogDismiss() {
        _entryToDelete.value = null
    }

    fun onDeleteConfirm() {
        _entryToDelete.value?.let { entry ->
            deleteTransaction(entry)
            onDeleteDialogDismiss()
        }
    }

    fun onEditEntryClick(entry: FinancialEntry) {
        _editDialogState.update {
            it.copy(
                entryToEdit = entry,
                description = entry.description,
                rawValue = entry.value.toString(),
                date = entry.date,
                type = entry.type,
                isDescriptionError = false,
                isValueError = false
            )
        }
    }

    fun onEditDialogDismiss() {
        _editDialogState.update { EditDialogState() }
    }

    fun onEditDescriptionChange(newDescription: String) {
        _editDialogState.update { it.copy(description = newDescription, isDescriptionError = false) }
    }

    fun onEditRawValueChange(newRawValue: String) {
        if (newRawValue.all { it.isDigit() }) {
            _editDialogState.update { it.copy(rawValue = newRawValue, isValueError = false) }
        }
    }

    fun onEditDateChange(newDateMillis: Long?) {
        newDateMillis?.let { millis ->
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val formattedNewDate = dateFormat.format(Date(millis))
            _editDialogState.update {
                it.copy(date = formattedNewDate, showDatePicker = false)
            }
        }
    }

    fun onEditTypeChange(newType: String) {
        _editDialogState.update { it.copy(type = newType) }
    }

    fun onShowDatePicker(show: Boolean) {
        _editDialogState.update { it.copy(showDatePicker = show) }
    }

    fun onEditConfirm() {
        val currentState = _editDialogState.value
        val descriptionValid = currentState.description.isNotBlank()
        val valueAsLong = currentState.rawValue.toLongOrNull() ?: 0L
        val valueValid = valueAsLong > 0

        val isError = !descriptionValid || !valueValid
        _editDialogState.update {
            it.copy(
                isDescriptionError = !descriptionValid,
                isValueError = !valueValid
            )
        }

        if (!isError) {
            val updatedEntry = currentState.entryToEdit!!.copy(
                description = currentState.description,
                value = valueAsLong,
                date = currentState.date,
                type = currentState.type
            )

            updateTransaction(updatedEntry)

            onEditDialogDismiss()
        }
    }


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