package com.example.financeflow.ui.main

import android.icu.util.Calendar

data class MainUiState(
    val monetaryValue: Long = 0L,
    val description: String = "",
    val transactionType: String = "Crédito",
    val selectedDateMillis: Long = getTodayMillis(),
    val isTypeDropdownExpanded: Boolean = false,
    val showDatePicker: Boolean = false,
    val typeOptions: List<String> = listOf("Crédito", "Débito"),
    val isDescriptionError: Boolean = false,
    val isValueError: Boolean = false
)

private fun getTodayMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}