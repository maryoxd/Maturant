package com.example.maturant.ui.theme


import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

object CommonComponents {

    @Composable
    fun BulletPoint(icon: ImageVector, size: Int) {
        Icon(
            imageVector = icon,
            contentDescription = "Bullet point",
            modifier = Modifier.size(size.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }


}