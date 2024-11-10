package pt.ipp.estg.geocaching_cultural.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesFound
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun HistoryScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {

    val user by usersViewsModels.currentUserId.observeAsState()

    val geocacheFound = UserGeocacheFoundCrossRef(user!!, 2)
    usersViewsModels.insertUserGeocacheFound(geocacheFound) /* TODO: isto deve ser feito quando um geocache é encontrado quando estiver implementado retirar daqui*/

    val geocachesFound = user?.let { usersViewsModels.getUserWithGeocachesFound(it).observeAsState() }?.value

    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
        Title(text = "Histórico Geocaches")

        if (geocachesFound != null) {
        Column {
            geocachesFound.geocachesFound.forEachIndexed { index, geocache -> /* TODO: quando souber como ir buscar imagens e outros dados do googleplaces necessário alterar*/
                VerticalSpacer()

                GeocacheCard(
                    title = "Mercadona",
                    description = "Sítio onde se faz compras",
                    points = 52,
                    image = painterResource(
                        R.drawable.mercadona
                    )
                )
            }

        }
        } else {
            Column {
                Text("Carregando histórico de geocaches...")
            }
        }
    }
}

@Preview
@Composable
fun HistoryPreview() {
    val navController = rememberNavController()

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                //HistoryScreen(navController)
            }
        }
    }
}