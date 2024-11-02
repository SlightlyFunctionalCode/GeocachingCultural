package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.utils.GeocacheHistoryCard
import pt.ipp.estg.geocashing_cultural.ui.utils.Title
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer

@Composable
fun HistoryScreen(navController: NavHostController) {
    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
        Title(text = "Histórico Geocaches")

        LazyColumn {
            item {
                VerticalSpacer()
            }

            item {
                GeocacheHistoryCard(
                    title = "Mercadona",
                    description = "Sítio onde se faz compras",
                    points = 52,
                    image = painterResource(
                        R.drawable.mercadona
                    )
                )
                VerticalSpacer()
            }

            item {
                GeocacheHistoryCard(
                    title = "Mercadona",
                    description = "Sítio onde se faz compras",
                    points = 52,
                    image = painterResource(
                        R.drawable.mercadona
                    )
                )
                VerticalSpacer()
            }

            item {
                GeocacheHistoryCard(
                    title = "Mercadona",
                    description = "Sítio onde se faz compras",
                    points = 52,
                    image = painterResource(
                        R.drawable.mercadona
                    )
                )
                VerticalSpacer()
            }
        }
    }
}