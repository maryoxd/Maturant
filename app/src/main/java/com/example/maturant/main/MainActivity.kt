package com.example.maturant.main

import com.example.maturant.maturitaScreens.ResultsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.maturant.maturitaScreens.MaturitaTestScreen
import com.example.maturant.maturitaScreens.TestScreen
import com.example.maturant.topics.GrammarTopicsScreen
import com.example.maturant.topics.LiteratureTopicsScreen
import com.example.maturant.ui.theme.MaturantTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import com.example.maturant.topics.DetailScreen
import com.example.maturant.viewModels.MaturitaViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaturantTheme {
                val navController = rememberNavController()
                val viewModelMaturitaViewModel: MaturitaViewModel = viewModel()
                NavHost(navController = navController, startDestination = "MainScreen") {
                    composable("MainScreen") { MainScreen(navController) }
                    composable("GrammarTopicsScreen") { GrammarTopicsScreen(navController) }
                    composable("LiteratureTopicsScreen") { LiteratureTopicsScreen(navController) }
                    composable("MaturitaTestScreen") { MaturitaTestScreen(navController, viewModelMaturitaViewModel) }
                    composable("TestScreen") {
                        TestScreen(navController, viewModelMaturitaViewModel)
                    }
                    composable("DetailScreen/{style}/{source}/{colorName}") { backStackEntry ->
                        DetailScreen(
                            style = backStackEntry.arguments?.getString("style") ?: "",
                            source = backStackEntry.arguments?.getString("source") ?: "",
                            colorName = backStackEntry.arguments?.getString("colorName") ?: "Green",
                            navController = navController
                        )
                    }
                    composable("ResultsScreen") { ResultsScreen(navController, LocalContext.current) }
                    composable("InfoScreen") { InfoScreen(navController) }
                }
            }
        }
    }
}
