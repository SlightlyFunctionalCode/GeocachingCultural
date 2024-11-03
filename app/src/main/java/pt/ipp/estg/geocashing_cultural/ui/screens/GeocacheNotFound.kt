package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.Pink
import pt.ipp.estg.geocashing_cultural.ui.theme.White
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow
import pt.ipp.estg.geocashing_cultural.ui.utils.LargeVerticalSpacer
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer

@Composable
fun GeocacheNotFoundScreen(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .background(shape = RoundedCornerShape(10.dp), color = White)
                .padding(28.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box() {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = "",
                            tint = Pink,
                            modifier = Modifier.size(32.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            modifier = Modifier.size(32.dp).scale(1.3f)
                        )
                    }
                    Text(
                        text = "Geocache Não Encontrado",
                        color = Pink,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LargeVerticalSpacer()
                Text(text = "Infelizmente, não acertou a pergunta desafio. \nNão ganhou pontos desta vez, mas pode tentar de novo!")
                VerticalSpacer()
                MyTextButton(text = "Voltar atrás")
            }
        }
    }
}

@Preview
@Composable
fun GeocacheNotFoundPreview() {
    val navController = rememberNavController()

    Geocashing_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow) // Set the background color here
        ) {
            item {
                GeocacheNotFoundScreen(navController)
            }
        }
    }
}