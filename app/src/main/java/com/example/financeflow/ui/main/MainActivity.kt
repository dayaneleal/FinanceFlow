package com.example.financeflow.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeflow.ui.components.DatePicker
import com.example.financeflow.ui.components.TextInput
import com.example.financeflow.ui.components.TypePicker
import com.example.financeflow.ui.components.valueinput.ValueInput
import com.example.financeflow.ui.theme.FinanceFlowTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceFlowTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    "Finance Flow",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    MainForm(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainForm(name: String, viewModel: MainViewModel = viewModel(), modifier: Modifier = Modifier) {

    val padding = Modifier.padding(start = 16.dp, end = 16.dp)

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        ValueInput(
            value = viewModel.uiState.monetaryValue.toString(),
            onValueChange = { stringValue ->
                viewModel.onMonetaryValueChange(stringValue.toLongOrNull() ?: 0L)
            },
            modifier = padding,
        )
        Spacer(modifier = Modifier.height(32.dp))
        TypePicker(
            selectedOption = viewModel.uiState.transactionType,
            options = viewModel.uiState.typeOptions,
            expanded = viewModel.uiState.isTypeDropdownExpanded,
            onExpandedChange = viewModel::onExpandTypeDropdown,
            onOptionSelected = viewModel::onTransactionTypeChange,
            modifier = padding,
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextInput(
            value = viewModel.uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            modifier = padding,
        )
        Spacer(modifier = Modifier.height(8.dp))
        DatePicker(
            showDatePicker = viewModel.uiState.showDatePicker,
            onShowDatePicker = viewModel::onShowDatePicker,
            onDateSelected = {},
            modifier = padding)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = padding.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Lançar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = padding
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp)
        ) {
            Text(text = "Ver lançamentos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinanceFlowPreview() {
    FinanceFlowTheme {
        MainForm("Android")
    }
}