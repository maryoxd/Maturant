package com.example.maturant.maturitaScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, context: Context) {
    val resultsFile = File(context.filesDir, "test_results.txt")
    val results = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    Log.d("ResultsScreen", "Results: $results")
    Log.d("ResultsScreen", "som tu")
    LaunchedEffect(Unit) {
        if (resultsFile.exists()) {
            resultsFile.forEachLine { line ->
                results.add(line)
            }
        }
    }

    fun deleteResults() {
        if (resultsFile.exists()) {
            resultsFile.delete()
            results.clear()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Výsledky testov") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                results.forEach { result ->
                    val (year, correct, total, percentage) = result.split(",")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            val successPercentageString = "%.2f".format(Locale.US, percentage.toDouble())
                            val currentLocale = Locale.getDefault() // Get system locale
                            val formatter = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale) // Formatter for date with locale
                            val formattedDate = formatter.format(Date()) // Format current date with locale
                            Text("Rok: $year", fontWeight = FontWeight.Bold)
                            Text("Správne odpovede: $correct z $total")
                            Text("Úspešnosť: $successPercentageString%")
                            Text("Dátum: $formattedDate")

                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (results.isNotEmpty()) {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Vymazať výsledky")
                    }
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Potvrdenie") },
            text = { Text("Naozaj chcete vymazať všetky výsledky?") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteResults()
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(Color.Green)
                ) {
                    Text("Áno")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text("Nie")
                }
            }
        )
    }
}
