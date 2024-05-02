package com.example.maturant.Topics

import com.example.maturant.ViewModels.SharedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.maturant.R
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.ui.theme.CommonComponents.MenuItemContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiteratureTopicsScreen(navController: NavController, viewModel: SharedViewModel = viewModel() ) {
    val context = LocalContext.current
    val resourceId = R.raw.literature_styles

    LaunchedEffect(key1 = resourceId) {
        viewModel.loadStyles(context, resourceId)
    }

    if(viewModel.isLoading.value) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
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
                                "LITERATÚRA",
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = AppColors.LightYellow
                    )
                )
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding()
                        ),

                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(viewModel.styles.value) { index, style ->
                        val colorName = if (index % 2 == 0) "Blue" else "Green"
                        MenuItemContent(style, AppColors.colorsMap[colorName] ?: AppColors.Green) {
                            if (!viewModel.isNavigationLocked.value) {
                                viewModel.lockNavigation()
                                navController.navigate("DetailScreen/$style/literary/$colorName")
                            }
                        }
                    }

                }
            }
        )
    }
}