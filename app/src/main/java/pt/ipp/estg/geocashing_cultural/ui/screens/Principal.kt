package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.utils.MyGeocachePertoDeMim
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextButton

@Composable
fun PrincipalScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo o espaço disponível em largura e altura
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
        Spacer(modifier = Modifier.height(25.dp))

        GeocachesPerto(navController)
    }
}

@Composable
fun GeocachesPerto(navController: NavHostController) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item { // Alterar este número conforme necessário
            MyGeocachePertoDeMim(
                "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas.\"",
                "5.0Km",
                painterResource(id = R.drawable.gastronomia)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        item { // Alterar este número conforme necessário
            MyGeocachePertoDeMim(
                "\"Sua fachada imponente e suas pedras centenárias contam histórias de fé e tradição.\"",
                "5.5Km",
                painterResource(id = R.drawable.historico)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        item { // Alterar este número conforme necessário
            MyGeocachePertoDeMim(
                "\"Edificício com heróis que lutampelo nosso país.\"",
                "5.6Km",
                painterResource(id = R.drawable.cultural)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        item { // Alterar este número conforme necessário
            MyGeocachePertoDeMim(
                "\"Café muito frequentemente visitado por estudantes.\"",
                "5.7Km",
                painterResource(id = R.drawable.gastronomia)
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            MyTextButton("+ Mais Geocaches", onClick = {
                navController.navigate("explorarScreen")
            },
                modifier = Modifier
                    .height(48.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PrincipalScreenPreview() {
    Geocashing_CulturalTheme {
        val navController = rememberNavController()
        PrincipalScreen(navController)
    }
}

