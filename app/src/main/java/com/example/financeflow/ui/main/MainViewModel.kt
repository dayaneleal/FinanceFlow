package com.example.financeflow.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun onDescriptionChange(newDescription: String) {
        uiState = uiState.copy(description = newDescription)
    }

    fun onMonetaryValueChange(newValue: Long) {
        uiState = uiState.copy(monetaryValue = newValue)
    }

    fun onTransactionTypeChange(newType: String) {
        uiState = uiState.copy(transactionType = newType, isTypeDropdownExpanded = false)
    }

    fun onDateChange(newDate: Long?) {
        uiState = uiState.copy(selectedDateMillis = newDate, showDatePicker = false)
    }

    fun onExpandTypeDropdown(isExpanded: Boolean) {
        uiState = uiState.copy(isTypeDropdownExpanded = isExpanded)
    }

    fun onShowDatePicker(show: Boolean) {
        uiState = uiState.copy(showDatePicker = show)
    }

    fun onSaveTransaction() {
        println("Salvando: ${uiState}")
    }
}
