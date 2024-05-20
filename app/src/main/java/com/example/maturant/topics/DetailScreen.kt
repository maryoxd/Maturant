package com.example.maturant.topics
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

import com.example.maturant.ui.theme.AppColors
import com.example.maturant.viewModels.SharedViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(style: String, source: String, colorName: String, navController: NavController, viewModel: SharedViewModel = viewModel()) {
    val color = AppColors.colorsMap[colorName] ?: Color.Green

    val context = LocalContext.current

    LaunchedEffect(key1 = style) {
        if (source == "grammatical" && viewModel.styles.value.isEmpty()) {
            viewModel.loadStyles(context)
        } else if (source == "literary" && viewModel.authors.value.isEmpty()) {
            viewModel.loadAuthors(context)
        }
    }

    val styleInfo = if (source == "grammatical") {
        viewModel.styles.value[style]
    } else {
        viewModel.authors.value[style]
    }

    Log.d("DetailScreen", "DetailScreen params - style: $style, source: $source, colorName: $colorName")
    Log.d("DetailScreen", "Available styles keys: ${viewModel.styles.value.keys}")
    Log.d("DetailScreen", "Available authors keys: ${viewModel.authors.value.keys}")
    Log.d("DetailScreen", "Loaded info: $styleInfo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (source == "grammatical") "GRAMATIKA" else "LITERATÚRA",
                        fontWeight = FontWeight.Bold,
                        color = AppColors.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = color),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            if (viewModel.isLoadingStyles.value || viewModel.isLoadingAuthors.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = style,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = AppColors.White
                            )
                        }
                    }
                    styleInfo?.let { info ->
                        item {
                            CategoryBox(if (source == "grammatical") "O ŠTÝLE" else "O AUTOROVI", info.about, color)
                            CategoryBox(if (source == "grammatical") "ZNAKY" else "DIELA", info.features, color)
                        }
                    } ?: run {
                        item {
                            Text(
                                text = "Informácie nie sú dostupné",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp),
                                color = AppColors.Red
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CategoryBox(title: String, content: String, appColors: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(appColors)
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = content,
            fontSize = 16.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}

