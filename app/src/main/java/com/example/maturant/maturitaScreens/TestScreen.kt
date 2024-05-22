package com.example.maturant.maturitaScreens




import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maturant.viewModels.MaturitaViewModel


/**
 * TestScreen
 * TestScreen slúži ako obrazovka s maturitným testom.
 * @param navController - Parameter navController slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 * @param viewModel - Parameter MaturitaViewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami, načítavanie testov, či uchovávanie viacerých stavov, zároveň aj ukladá odpovede a vyhodnocuje ich.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(navController: NavController, viewModel: MaturitaViewModel = viewModel()) {
    val isLoading by viewModel.isLoading.collectAsState()
    val test = viewModel.currentTest.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadTest(context, "test_" + viewModel.selectedYear.value + ".json")
        viewModel.isTimeUpDialogShown.value = false
    }

    val callback = rememberUpdatedState(newValue = {
        viewModel.resetTimer()
        navController.navigateUp()
        viewModel.resetResults()
        viewModel.isTestSubmitted.value = false
        viewModel.wasSaved.value = false
    })

    val showExitDialog = remember { mutableStateOf(false) }
    val showTimeUpDialog = remember { mutableStateOf(false) }

    val minutes = viewModel.remainingTime.collectAsState().value / 60
    val seconds = viewModel.remainingTime.collectAsState().value % 60
    val answered = viewModel.answeredQuestions.collectAsState().value
    val total = viewModel.totalQuestions.collectAsState().value

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    if (minutes == 0 && seconds == 0 && !viewModel.isTimeUpDialogShown.value) {
        viewModel.pauseTimer()
        showTimeUpDialog.value = true
        viewModel.isTimeUpDialogShown.value = true
    }

    if(viewModel.isTestSubmitted.value) {
        viewModel.pauseTimer()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(90.dp),
                title = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 40.dp, top = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "MATURITNÝ TEST",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.White
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "ROK: ${viewModel.selectedYear.value}",
                                color = AppColors.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                "ČAS: ${minutes.toString().padStart(2, '0')}:${
                                    seconds.toString().padStart(2, '0')
                                }",
                                color = AppColors.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                "OTÁZKY: $answered/$total",
                                color = AppColors.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (answered > 0) {
                            viewModel.pauseTimer()
                            if (!viewModel.isTestSubmitted.value) {
                                showExitDialog.value = true
                            } else {
                                callback.value.invoke()
                            }
                        } else {
                            callback.value.invoke()
                        }
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            Modifier.padding(top = 20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.TuftsBlue)
            )
        },
        content = { innerPadding ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.TuftsBlue)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    state = listState
                ) {
                    item {
                        if (test != null) {
                            DisplayTest(test, viewModel)
                        } else {
                            Text(
                                "Test nebol nájdený",
                                color = AppColors.White,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    )
    ExitConfirmationDialog(showExitDialog, navController, viewModel)
    TimeUpDialog(showTimeUpDialog, viewModel)
}

/**
 * ExitConfirmationDialog
 * ExitConfirmationDialog slúži na zobrazenie dialógu, či chce užívateľ naozaj odísť, pokiaľ odpovedal aspoň na 1 otázku.
 * @param showExitDialog - Parameter showExitDialog slúži na zobrazenie dialógu, pričom si uchováva svoj stav.
 * @param navController - Parameter navController slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 * @param viewModel - Parameter MaturitaViewModel slúži na zdieľanie dát medzi jednotlivými obrazovkami, načítavanie testov, či uchovávanie viacerých stavov, zároveň aj ukladá odpovede a vyhodnocuje ich.
 */
@Composable
fun ExitConfirmationDialog(
    showExitDialog: MutableState<Boolean>,
    navController: NavController,
    viewModel: MaturitaViewModel
) {
    if (showExitDialog.value) {
        AlertDialog(
            onDismissRequest = { showExitDialog.value = false },
            title = { Text("Potvrdenie") },
            text = { Text("Naozaj chcete odísť? Vaše odpovede nebudú uložené.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetTimer()
                        navController.navigateUp()
                        viewModel.resetResults()
                        viewModel.isTestSubmitted.value = false
                        showExitDialog.value = false
                        viewModel.isTimeUpDialogShown.value = false
                    },
                    colors = ButtonDefaults.buttonColors(AppColors.Red)
                ) {
                    Text("Odísť")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showExitDialog.value = false
                        viewModel.resumeTimer()
                    },
                    colors = ButtonDefaults.buttonColors(AppColors.Green)
                ) {
                    Text("Pokračovať")
                }
            }
        )
    }
}

/**
 * TimeUpDialog
 * TimeUpDialog slúži na zobrazenie dialógu, v prípade, že užívateľovi došiel čas. Vtedy sa automaticky vyhodnotí test a pozastaví sa čas.
 * @param showTimeUpDialog
 * @param viewModel
 */
@Composable
fun TimeUpDialog(
    showTimeUpDialog: MutableState<Boolean>,
    viewModel: MaturitaViewModel
) {
    if (showTimeUpDialog.value) {
        AlertDialog(
            onDismissRequest = { showTimeUpDialog.value = false },
            title = { Text("Čas vypršal") },
            text = { Text("Čas na dokončenie testu vypršal.") },
            confirmButton = {
                Button(
                    onClick = {
                        showTimeUpDialog.value = false
                        viewModel.submitTest()
                    },
                    colors = ButtonDefaults.buttonColors(AppColors.Green)
                ) {
                    Text("OK")
                }
            }
        )
    }
}
