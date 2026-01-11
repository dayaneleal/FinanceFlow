package com.example.financeflow.ui.main

data class MainUiState(
    val monetaryValue: Long = 0L,
    val description: String = "",
    val transactionType: String = "Crédito",
    val selectedDateMillis: Long? = null,
    val isTypeDropdownExpanded: Boolean = false,
    val showDatePicker: Boolean = false,
    val typeOptions: List<String> = listOf("Crédito", "Débito")
)