package com.example.maturant


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.ui.theme.BulletPoint


@Composable
fun MainScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_maturant),
                contentDescription = "Logo Maturant",
                modifier = Modifier
                    .height(250.dp)
                    .width(400.dp)
            )
            Spacer(modifier = Modifier.height(1.dp))
            MenuItem("Gramatické témy", AppColors.LightestGreen)  {navController.navigate("GrammarTopicsScreen")}
            MenuItem("Literatúrne témy", AppColors.LightGreen) {navController.navigate("literatureTopicsScreen")}
            MenuItem("Maturitné testy", AppColors.Green) {navController.navigate("maturitaTestsScreen")}
            Spacer(modifier = Modifier.height(24.dp))
            MenuItem("Výsledky", AppColors.Orange) {navController.navigate("resultsScreen")}

        }
    }
}

@Composable
fun MenuItem(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable (onClick = onClick)
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
            BulletPoint(Icons.Default.KeyboardArrowRight, 50)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy( fontWeight = FontWeight.Bold)
            )
        }
    }
}


