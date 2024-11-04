package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyGeocachePertoDeMim
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun PrincipalScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Felgueiras, Porto",
                fontSize = 14.sp, // Define o tamanho da fonte
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Geocaches Perto de Mim",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,// Define o tamanho da fonte para o texto maior
                modifier = Modifier.fillMaxWidth()
            )
        }
        VerticalSpacer()

        GeocachesPerto(navController)
    }
}

@Composable
fun GeocachesPerto(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Alterar este número conforme necessário
        MyGeocachePertoDeMim(
            "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas.\"",
            "5.0Km",
            painterResource(id = R.drawable.gastronomia)
        )

        SmallVerticalSpacer()
        // Alterar este número conforme necessário
        MyGeocachePertoDeMim(
            "\"A sua fachada imponente e as suas pedras centenárias contam histórias de fé e tradição.\"",
            "5.5Km",
            painterResource(id = R.drawable.historico)
        )

        SmallVerticalSpacer()
        // Alterar este número conforme necessário
        MyGeocachePertoDeMim(
            "\"Edificício com heróis que lutampelo nosso país.\"",
            "5.6Km",
            painterResource(id = R.drawable.cultural)
        )

        SmallVerticalSpacer()
        // Alterar este número conforme necessário
        MyGeocachePertoDeMim(
            "\"Café muito frequentemente visitado por estudantes.\"",
            "5.7Km",
            painterResource(id = R.drawable.gastronomia)
        )

        SmallVerticalSpacer()

        MyTextButton(
            "+ Mais Geocaches", onClick = {
                navController.navigate("explorarScreen")
            },
            modifier = Modifier
                .height(48.dp)
        )

    }
}


@Preview
@Composable
fun PrincipalPreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                PrincipalScreen(navController)
            }
        }
    }
}
