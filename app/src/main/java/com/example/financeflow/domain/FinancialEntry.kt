package com.example.financeflow.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "financial_entries")
data class FinancialEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: Long,
    val type: String,
    val description: String,
    val date: String
)