package pt.ipp.estg.geocashing_cultural.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray
import java.lang.reflect.Modifier

@Composable
fun Footer() {
    BottomAppBar(
        content = {
            Row() {
                Text(text = "© GeoCultura Explorer Inc 2024", color = LightGray, fontSize = 12.sp)
                Row() {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Home")
                    Icon(imageVector = Icons.Default.Build, contentDescription = "Home")
                }
            }
        },
    )
}