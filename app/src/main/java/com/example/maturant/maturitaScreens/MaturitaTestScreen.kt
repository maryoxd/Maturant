package com.example.maturant.maturitaScreens


import androidx.navigation.NavController
import com.example.maturant.viewModels.MaturitaViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maturant.ui.theme.AppColors


/**
 * MaturitaTestScreen
 * MaturitaTestScreen slúži na zobrazenie obrazovky s maturitnými testami.
 * @param navController - Parameter NavController slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 * @param viewModel - Parameter viewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami, načítavanie testov, či uchovávanie viacerých stavov, zároveň aj ukladá odpovede a vyhodnocuje ich.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaturitaTestScreen(navController: NavController, viewModel: MaturitaViewModel = viewModel()) {
    if (viewModel.showDialog.value) {
        TimerSetupDialog(
            year = viewModel.selectedYear.value,
            navController = navController,
            viewModel = viewModel
        )
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
                            "MATURITNÉ TESTY",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.TuftsBlue)
            )
        },
        content = { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(listOf("2018", "2019", "2022", "2023", "2024")) { year ->
                    YearItem(year = year, onClick = {
                        viewModel.onYearClick(year)
                    })
                }
            }
        }
    )
}

/**
 * YearItem
 * YearItem je jednoduchý composable element ktorý slúži na zobrazenie jednotlivých rokov testov v políčkach.
 * @param year - Parameter year slúži na zobrazenie konkrétneho roka testu.
 * @param onClick - Parameter onClick slúži na zavolanie funkcie, ktorá sa vykoná pri kliknutí na daný rok.
 */
@Composable
fun YearItem(year: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = year,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Detail",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * TimerSetupDialog
 * TimerSetupDialog slúži na zobrazenie dialógu, ktorý umožňuje nastaviť časovač pre daný rok.
 * @param year - Parameter year slúži na zobrazenie konkrétneho roka testu, aby bolo užívateľovi jasné na aký rok nastavuje časovač.
 * @param navController - Parameter navController slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 * @param viewModel - Parameter viewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami (v tomto prípade dialog).
 */
@Composable
fun TimerSetupDialog(year: String, navController: NavController, viewModel: MaturitaViewModel) {
    var minutesText by remember { mutableStateOf("90") }
    var secondsText by remember { mutableStateOf("0") }

    AlertDialog(
        onDismissRequest = { viewModel.hideDialog() },
        title = {
            Text("Nastaviť časovač pre rok $year", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                TextField(
                    value = minutesText,
                    onValueChange = { if (it.length <= 2 && it.all { char -> char.isDigit() }) minutesText = it },
                    label = { Text("Minúty") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                TextField(
                    value = secondsText,
                    onValueChange = { if (it.length <= 2 && it.all { char -> char.isDigit() }) secondsText = it },
                    label = { Text("Sekundy") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val minutes = minutesText.toIntOrNull() ?: 0
                    val seconds = secondsText.toIntOrNull() ?: 0
                    if (minutes > 0 || seconds > 0) {
                        viewModel.initTimerAndStart(year, minutes, seconds)
                        navController.navigate("TestScreen") {
                            popUpTo("MaturitaTestScreen") { inclusive = false }
                        }
                        viewModel.hideDialog()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Green)
            ) {
                Text("Pokračovať")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.hideDialog() }, colors = ButtonDefaults.buttonColors(containerColor = AppColors.Red)) {
                Text("Zrušiť")
            }
        }
    )
}