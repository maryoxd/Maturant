package com.example.maturant

import DetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maturant.GrammarScreen.GrammarTopicsScreen
import com.example.maturant.LiteratureScreen.LiteratureTopicsScreen
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.ui.theme.MaturantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaturantTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("MainScreen") { MainScreen(navController)}
                    composable("GrammarTopicsScreen") { GrammarTopicsScreen(navController) }
                    composable("LiteratureTopicsScreen") { LiteratureTopicsScreen(navController) }
                    composable("DetailScreen/{style}/{source}/{colorName}") { backStackEntry ->
                        DetailScreen(
                            style = backStackEntry.arguments?.getString("style") ?: "",
                            source = backStackEntry.arguments?.getString("source") ?: "unknown",
                            colorName = backStackEntry.arguments?.getString("colorName") ?: "Green",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}



