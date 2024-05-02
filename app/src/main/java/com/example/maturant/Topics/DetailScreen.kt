package com.example.maturant.Topics
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.maturant.Topics.Loading.AuthorInfo
import com.example.maturant.Topics.Loading.StyleInfo
import com.example.maturant.Topics.Loading.authorDetails
import com.example.maturant.Topics.Loading.styleDetails
import com.example.maturant.ui.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(style: String, source: String, colorName: String, navController: NavController) {
    val color = AppColors.colorsMap[colorName] ?: Color.Green
    val styleInfo = if (source == "grammatical") {
        styleDetails[style] ?: StyleInfo("Informácie nie sú dostupné", "Žiadne znaky")
    } else {
        authorDetails[style] ?: AuthorInfo("Informácie nie sú dostupné", "Žiadne znaky")
    }

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
                item {
                    CategoryBox(if(source == "grammatical") "O ŠTÝLE" else "O AUTOROVI", styleInfo.about, color)
                    CategoryBox(if(source == "grammatical") "ZNAKY" else "DIELA", styleInfo.features, color)
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


