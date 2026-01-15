package com.example.financeflow.ui.expensereport

import com.example.financeflow.domain.FinancialEntry

data class EditDialogState(
    val entryToEdit: FinancialEntry? = null,
    val description: String = "",
    val rawValue: String = "",
    val date: String = "",
    val type: String = "Débito",
    val showDatePicker: Boolean = false,
    val isDescriptionError: Boolean = false,
    val isValueError: Boolean = false
)