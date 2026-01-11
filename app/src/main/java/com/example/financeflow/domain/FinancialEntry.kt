package com.example.financeflow.domain

data class FinancialEntry(
    val value: String,
    val type: String,
    val description: String,
    val date: String
)