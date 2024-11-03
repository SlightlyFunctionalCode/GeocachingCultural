package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocashing_cultural.ui.utils.Title
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer

@Composable
fun CreatedGeocachesScreen(navController: NavHostController) {
    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
        Title(text = "Geocaches Criados")

        LazyColumn {
            item {
                VerticalSpacer()
            }

            item {
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

            item {
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

            item {
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
}