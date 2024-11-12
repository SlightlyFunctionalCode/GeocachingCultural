package pt.ipp.estg.geocaching_cultural.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.utils_api.fetchGeocacheImage
import pt.ipp.estg.geocaching_cultural.utils_api.getApiKey

@Composable
fun HistoryScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val context = LocalContext.current

    val user by usersViewsModels.currentUserId.observeAsState()

    val geocacheFound = UserGeocacheFoundCrossRef(user!!, 1)
    usersViewsModels.insertUserGeocacheFound(geocacheFound) /* TODO: isto deve ser feito quando um geocache é encontrado quando estiver implementado retirar daqui*/

    val geocachesFound = user?.let { usersViewsModels.getUserWithGeocachesFound(it).observeAsState() }?.value

    getApiKey(context)?.let { Places.initialize(context, it) }

    Column(Modifier.padding(28.dp)) {
        Title(text = "Histórico Geocaches")

        if (geocachesFound!= null) {
            Column {
                geocachesFound.geocachesFound.forEach { geocache ->
                    val image = fetchGeocacheImage(geocache, context)

                    GeocacheCard(
                        title = geocache.name,
                        description = geocache.address?: "Endereço desconhecido",
                        points = 52, // valor fixo ou dinâmico
                        image = image
                    )
                }
            }
        } else {
            Text("Carregando histórico de geocaches...")
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