package com.example.maturant.maturitaScreens



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maturant.viewModels.MaturitaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(navController: NavController, viewModel: MaturitaViewModel = viewModel()) {
    val callback = rememberUpdatedState(newValue = {
        viewModel.resetTimer()
        navController.navigateUp()
    })

    val minutes = viewModel.remainingTime.collectAsState().value / 60
    val seconds = viewModel.remainingTime.collectAsState().value % 60
    val answered = viewModel.answeredQuestions.collectAsState().value
    val total = viewModel.totalQuestions.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(end = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "MATURITNÝ TEST",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.White
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { callback.value.invoke() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.Blue)
            )
        },
        content = { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .background(AppColors.Blue),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,

                        ) {
                            Text("ROK: ${viewModel.selectedYear.value}", color = AppColors.White, fontWeight = FontWeight.Bold)
                            Text("ČAS: ${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}", color = AppColors.White, fontWeight = FontWeight.Bold)
                            Text("OTÁZKY: $answered/$total", color = AppColors.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    )
}

