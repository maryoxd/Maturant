package com.example.maturant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.maturant.ui.theme.MaturantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaturantTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") { MainScreen(navController)}
                    composable("grammarTopicsScreen") {GrammarTopicsScreen(navController)}

                }
            }
        }
    }
}


