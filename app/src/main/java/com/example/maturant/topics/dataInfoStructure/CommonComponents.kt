package com.example.maturant.topics.dataInfoStructure


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maturant.ui.theme.AppColors


/**
 * CommonComponents
 * CommonComponents slúži na uchovanie spoločných komponentov, ktoré sa používajú v rôznych častiach aplikácie.
 */
object CommonComponents {

    /**
     * BullettPoint
     * BulletPoint slúži na vytvorenie ikony, ktorá reprezentuje bodový zoznam.
     * @param icon - Parameter icon slúži ako ikona bodového zoznamu.
     * @param size - Parameter size slúži ako veľkosť ikony.
     */
    @Composable
    fun BulletPoint(icon: ImageVector, size: Int) {
        Icon(
            imageVector = icon,
            contentDescription = "Bullet point",
            modifier = Modifier.size(size.dp),
            tint = AppColors.White

        )
    }

    /**
     * MenuItemContent
     * MenuItemContent slúži na vytvorenie komponentu, ktorý reprezentuje položku v menu.
     * @param text - Parameter text slúži na zobrazenie textu položky.
     * @param backgroundColor - Parameter backgroundColor slúži na nastavenie farby pozadia položky.
     * @param onClick - Parameter onClick slúži na zavolanie funkcie po kliknutí na položku.
     */
    @Composable
    fun MenuItemContent(text: String, backgroundColor: Color, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
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
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = AppColors.White,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}