package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.CloseGeocache
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer
import java.time.LocalDateTime

@Composable
fun HomeScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels,
    usersViewsModels: UsersViewsModels
) {
    val currentUser = usersViewsModels.currentUser.observeAsState()

    val closeGeocachesWithHints =
        currentUser.value?.let {
            geocacheViewsModels.getClosestGeocaches(it.location).observeAsState()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                /* TODO: Corrigir Localização Atual */
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

        Closest5Geocaches(closeGeocachesWithHints?.value, navController)
    }
}

@Composable
fun Closest5Geocaches(
    closest5Geocaches: List<Pair<GeocacheWithHintsAndChallenges, Double>>?,
    navController: NavHostController,
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        if (closest5Geocaches.isNullOrEmpty()) {
            Text("Pedimos desculpa, não existem Geocaches numa àrea de 10 Km!")
            SmallVerticalSpacer()
        } else {
            /* TODO: calcular distância */
            for (geocacheAndDistance in closest5Geocaches) {

                CloseGeocache(
                    hint = if (geocacheAndDistance.first.hints.isNotEmpty()) geocacheAndDistance.first.hints[0].hint else "Sem dicas",
                    distance = "${geocacheAndDistance.second/1000} Km",
                    icon = when (geocacheAndDistance.first.geocache.type) {
                        GeocacheType.GASTRONOMIA -> painterResource(R.drawable.gastronomia)
                        GeocacheType.CULTURAL -> painterResource(R.drawable.cultural)
                        GeocacheType.HISTORICO -> painterResource(R.drawable.historico)
                    },
                    onClick = {} /* TODO: redirecionar para iniciar geocache */
                )

                SmallVerticalSpacer()
            }
        }

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
                //  HomeScreen(navController)
            }
        }
    }
}
