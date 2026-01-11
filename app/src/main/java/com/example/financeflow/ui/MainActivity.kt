package com.example.financeflow.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.financeflow.ui.expensereport.ExpenseReportScreen
import com.example.financeflow.ui.main.MainScreen
import com.example.financeflow.ui.theme.FinanceFlowTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinanceFlowTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        MainScreen(
                            onNavigate = { navController.navigate("expenseReport") }
                        )
                    }
                    composable("expenseReport") {
                        ExpenseReportScreen(
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
