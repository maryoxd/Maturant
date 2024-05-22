package com.example.maturant.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * InfoScreen
 * InfoScreen slúži ako unikátna obrazovka na zobrazovanie základných informácií o programe.
 * Obsahuje LaunchedEffect na načítanie dát z .txt súborov (info.txt, zdroje.txt) a zobrazuje ich v LazyColumn.
 * @param navController - slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(navController: NavController) {
    val context = LocalContext.current

    var content1 by remember { mutableStateOf("") }
    var content2 by remember { mutableStateOf("") }

    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    LaunchedEffect(Unit) {
        content1 = loadData(context, "info.txt")
        content2 = loadData(context, "zdroje.txt")
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
                            "INFORMÁCIE",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AppColors.MarianBlue)
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(10.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        TitleBox(title = "ZÁKLADNÉ INFORMÁCIE", content = content1)
                        Spacer(modifier = Modifier.height(16.dp))
                        TitleBox(title = "ZDROJE PRÁCE", content = content2)
                        Spacer(modifier = Modifier.height(16.dp))
                        TitleBox(title = "Mário Žilinčík 2024", content = "")
                    }
                }
            }
        }
    )
}

/**
 * TitleBox
 * TitleBox je len jednoduchý Composable element, ktorý slúži na zobrazenie nadpisu a textu.
 * @param title - Parameter title slúži ako samotný nadpis textu.
 * @param content - Parameter content slúži ako text ktorý sa vypíše pod nadpis.
 */
@Composable
fun TitleBox(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            color = AppColors.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(AppColors.MarianBlue)
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

/**
 * LoadData
 * LoadData je funkcia, ktorá slúži na načítanie obsahu súboru z assets.
 * @param context - Parameter context slúži na získanie kontextu aplikácie.
 * @param fileName - Parameter fileName slúži na získanie názvu súboru, ktorý sa má načítať.
 * @return
 */
fun loadData(context: Context, fileName: String): String {
    return try {
        val inputStream = context.assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.use { it.readText() }
    } catch (e: Exception) {
        "Nepodarilo sa načítať obsah súboru."
    }
}
