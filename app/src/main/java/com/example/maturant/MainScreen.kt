package com.example.maturant


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.ui.theme.CommonComponents.BulletPoint
import com.example.maturant.viewModels.SharedViewModel


@Composable
fun MainScreen(navController: NavController, viewModel: SharedViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = "Pozadie",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.main_logo),
                    contentDescription = "Logo Maturant",
                    modifier = Modifier
                        .height(250.dp)
                        .width(600.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(1.dp)) }
            items(listOf("Gramatické témy", "Literatúrne témy", "Maturitné testy", "Výsledky")) { text ->
                val color = when (text) {
                    "Gramatické témy" -> AppColors.LightestGreen
                    "Literatúrne témy" -> AppColors.LightGreen
                    "Maturitné témy" -> AppColors.Green
                    "Výsledky" -> AppColors.Orange
                    else -> AppColors.LightGreen
                }
                val navigation = when (text) {
                    "Gramatické témy" -> "GrammarTopicsScreen"
                    "Literatúrne témy" -> "LiteratureTopicsScreen"
                    "Maturitné testy" -> "MaturitaTestScreen"
                    "Výsledky" -> "ResultsScreen"
                    else -> "GrammarTopicsScreen"
                }
                MenuItem(text, color) {
                    if (!viewModel.isNavigationLocked.value) {
                        viewModel.lockNavigation()
                        navController.navigate(navigation)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 0.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RectangleShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BulletPoint(Icons.AutoMirrored.Filled.KeyboardArrowRight, 50)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppColors.White)
            )
        }
    }
}



