package com.example.maturant.maturitaScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import java.io.File
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(navController: NavController, context: Context) {
    val resultsFile = File(context.filesDir, "test_results.txt")
    val results = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }

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
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "VÝSLEDKY TESTOV",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.Azul)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                results.forEach { result ->
                    val (year, correct, total, percentage, date) = result.split(",")
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            val successPercentageString = "%.2f".format(Locale.US, percentage.toDouble())
                            Text("Rok: $year", fontWeight = FontWeight.Bold)
                            Text("Správne odpovede: $correct z $total")
                            Text("Úspešnosť: $successPercentageString%")
                            Text("Dátum: $date")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (results.isNotEmpty()) {
                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(AppColors.Red),
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
                    colors = ButtonDefaults.buttonColors(AppColors.SystemGreen)
                ) {
                    Text("Áno")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(AppColors.Red)
                ) {
                    Text("Nie")
                }
            }
        )
    }
}
