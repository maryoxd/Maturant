package com.example.maturant.main


import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.maturant.R
import com.example.maturant.ui.theme.AppColors
import com.example.maturant.viewModels.SharedViewModel


/**
 * Main screen
 * MainScreen slúži ako hlavná obrazovka aplikácie, ktorá obsahuje navigáciu na jednotlivé obrazovky.
 * @param navController - slúži na navigáciu pomocou NavControllera, aby bolo možné sa presúvať zo screeny naspäť a sem.
 * @param viewModel - slúži na zdieľanie dát medzi jednotlivými obrazovkami.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: SharedViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { navController.navigate("InfoScreen") }) {
                        Icon(Icons.Default.Info, contentDescription = "Informácie")
                    }
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                                .width(650.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                    // Lambda výraz zoberie všetky MenuItemInfo a v LazyItemScope cez ne iteruje a spracuje ich ako parameter pre MenuItem.
                    items(
                        listOf(
                            MenuItemInfo("GRAMATICKÉ TÉMY", AppColors.UranianBlue, "GrammarTopicsScreen"),
                            MenuItemInfo("LITERATÚRNE TÉMY", AppColors.SeaGreen, "LiteratureTopicsScreen"),
                            MenuItemInfo("MATURITNÉ TESTY", AppColors.TuftsBlue, "MaturitaTestScreen"),
                            MenuItemInfo("VÝSLEDKY", AppColors.Azul, "ResultsScreen")
                        )
                    ) { menuItem ->
                        MenuItem(menuItem) {
                            if (!viewModel.isNavigationLocked.value) {
                                viewModel.lockNavigation()
                                navController.navigate(menuItem.navigationDestination)
                            }
                        }
                    }
                }
            }
        }
    )
}


/**
 * MenuItemInfo
 * MenuItemInfo slúži ako pomocná data class pomocou ktorej neskôr vykreslujeme políčka pre dané témy (Gramatika, Literatúra, Testy, Výsledky)
 * @property text - Text ktorý bude zobrazený v políčku.
 * @property backgroundColor - Farba pozadia políčka.
 * @property navigationDestination - Destinácia na ktorú sa má presmerovať po kliknutí na dané políčko.
 * @constructor - Vytvára novú inštanciu MenuItemInfo.
 */
data class MenuItemInfo(val text: String, val backgroundColor: Color, val navigationDestination: String)

/**
 * MenuItem
 * MenuItem už slúži ako Composable element, ktorý zobrazuje jednotlivé políčka s témami.
 * @param menuItem - Parameter menuItem si zoberie dáta z atribútov MenuItemInfo.
 * @param onClick - Parameter onClick slúži na zavolanie funkcie po kliknutí na dané políčko.
 */
@Composable
fun MenuItem(menuItem: MenuItemInfo, onClick: () -> Unit) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        colors =  CardDefaults.cardColors(menuItem.backgroundColor),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Bullet(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow",
                tint = AppColors.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = menuItem.text,
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = AppColors.White,
                )
            )
        }
    }
}

/**
 * Bullet
 * Bullet slúži ako Composable element, ktorý zobrazuje šípku pred textom.
 * @param imageVector - Parameter imageVector slúži na zobrazenie šípky.
 * @param contentDescription - Parameter contentDescription slúži na popis šípky.
 * @param tint - Parameter tint slúži na zmenu farby šípky.
 */
@Composable
fun Bullet(imageVector: ImageVector, contentDescription: String, tint: Color) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint,
    )
}



