package pt.ipp.estg.geocashing_cultural.ui.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer(){
    Spacer(Modifier.width(28.dp))
}

@Composable
fun VerticalSpacer(){
    Spacer(Modifier.height(28.dp))
}

@Composable
fun SmallHorizontalSpacer(){
    Spacer(Modifier.width(14.dp))
}

@Composable
fun SmallVerticalSpacer(){
    Spacer(Modifier.height(14.dp))
}

@Composable
fun LargeHorizontalSpacer(){
    Spacer(Modifier.width(56.dp))
}

@Composable
fun LargeVerticalSpacer(){
    Spacer(Modifier.height(56.dp))
}