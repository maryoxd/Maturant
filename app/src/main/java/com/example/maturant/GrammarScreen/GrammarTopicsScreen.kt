package com.example.maturant.GrammarScreen

import SharedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.maturant.R
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.ui.theme.CommonComponents.BulletPoint


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarTopicsScreen(navController: NavController, viewModel: SharedViewModel = viewModel() ) {
    val context = LocalContext.current
    val resourceId = R.raw.grammar_styles

    LaunchedEffect(key1 = resourceId) {
        viewModel.loadStyles(context, resourceId)
    }
    val styles = viewModel.styles
    if (styles.value.isEmpty()) {
        CircularProgressIndicator()
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
                                "GRAMATIKA",
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
                    itemsIndexed(styles.value) { index, style ->
                        GrammarMenuItem(style, if (index % 2 == 0) AppColors.Blue else AppColors.Green)
                    }

                }
            }
        )
    }
}

@Composable
fun GrammarMenuItem(text: String, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BulletPoint(Icons.Default.KeyboardArrowUp, 32)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}


