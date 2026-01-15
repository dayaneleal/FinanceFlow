package com.example.financeflow.ui.main

import android.app.Application
import android.icu.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.financeflow.data.AppDatabase
import com.example.financeflow.data.FinancialEntryDao
import com.example.financeflow.domain.FinancialEntry
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(private val dao: FinancialEntryDao) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set


    fun onDescriptionChange(newDescription: String) {
        uiState = uiState.copy(
            description = newDescription,
            isDescriptionError = false
        )
    }


    fun onMonetaryValueChange(newValue: Long) {
        uiState = uiState.copy(
            monetaryValue = newValue,
            isValueError = false
        )
    }

    fun onTransactionTypeChange(newType: String) {
        uiState = uiState.copy(transactionType = newType, isTypeDropdownExpanded = false)
    }

    fun onDateChange(newDate: Long) {
        uiState = uiState.copy(selectedDateMillis = newDate, showDatePicker = false)
    }

    fun onExpandTypeDropdown(isExpanded: Boolean) {
        uiState = uiState.copy(isTypeDropdownExpanded = isExpanded)
    }

    fun onShowDatePicker(show: Boolean) {
        uiState = uiState.copy(showDatePicker = show)
    }


    fun onSaveTransaction() {
        val isDescriptionValid = uiState.description.isNotBlank()
        val isValueValid = uiState.monetaryValue > 0


        if (!isDescriptionValid || !isValueValid) {
            uiState = uiState.copy(
                isDescriptionError = !isDescriptionValid,
                isValueError = !isValueValid
            )
            return
        }

        val dateString = uiState.selectedDateMillis?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
        } ?: SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val entry = FinancialEntry(
            value = (uiState.monetaryValue),
            type = uiState.transactionType,
            description = uiState.description,
            date = dateString
        )

        viewModelScope.launch {
            dao.insert(entry)

            uiState = MainUiState()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                val database = AppDatabase.getDatabase(application)
                MainViewModel(database.financialEntryDao())
            }
        }
    }
}