package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow
import pt.ipp.estg.geocashing_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocashing_cultural.ui.utils.Title
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer

@Composable
fun CreatedGeocachesScreen(navController: NavHostController) {
    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
        Title(text = "Geocaches Criados")

        Column {
            VerticalSpacer()

            GeocacheCard(
                title = "Mercadona",
                description = "Sítio onde se faz compras",
                image = painterResource(
                    R.drawable.mercadona
                ),
                modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen") }
            )
            VerticalSpacer()

            GeocacheCard(
                title = "Mercadona",
                description = "Sítio onde se faz compras",
                image = painterResource(
                    R.drawable.mercadona
                ),
                modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen") }

            )
            VerticalSpacer()

            GeocacheCard(
                title = "Mercadona",
                description = "Sítio onde se faz compras",
                image = painterResource(
                    R.drawable.mercadona
                ),
                modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen") }
            )
            VerticalSpacer()
        }
    }
}

@Preview
@Composable
fun CreatedGeocachesPreview() {
    val navController = rememberNavController()

    Geocashing_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                CreatedGeocachesScreen(navController)
            }
        }
    }
}