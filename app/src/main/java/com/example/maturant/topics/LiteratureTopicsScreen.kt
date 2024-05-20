package com.example.maturant.topics

import com.example.maturant.viewModels.SharedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.topics.dataInfoStructure.CommonComponents.MenuItemContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiteratureTopicsScreen(navController: NavController, viewModel: SharedViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadAuthors(context)
    }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        if (viewModel.isLoadingAuthors.value) {
            CircularProgressIndicator(
                color = AppColors.SeaGreen
            )
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
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.SeaGreen)
                    )
                },
                content = { innerPadding ->
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = innerPadding.calculateTopPadding()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(viewModel.authors.value.entries.toList()) { index, (authorName, _) ->
                            val colorName = if (index % 2 == 0) "Malachite" else "PigmentGreen"
                            MenuItemContent(
                                authorName,
                                AppColors.colorsMap[colorName] ?: AppColors.Malachite
                            ) {
                                if (!viewModel.isNavigationLocked.value) {
                                    viewModel.lockNavigation()
                                    navController.navigate("DetailScreen/$authorName/literary/$colorName")
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

