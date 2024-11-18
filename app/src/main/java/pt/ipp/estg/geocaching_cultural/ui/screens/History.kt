package pt.ipp.estg.geocaching_cultural.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.ChallengedGeocache
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import java.time.LocalDateTime

@Composable
fun HistoryScreen(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {

    val user by usersViewsModels.currentUserId.observeAsState()

    val geocacheCrossRef = ChallengedGeocache(user!!, 1, 123)
    usersViewsModels.insertChallengedGeocache(geocacheCrossRef) /* TODO: isto deve ser feito quando um geocache é encontrado quando estiver implementado retirar daqui*/

    val geocachesFound =
        usersViewsModels.getUserWithGeocachesFound(user!!).observeAsState()

    Column(Modifier.padding(28.dp)) {
        Title(text = stringResource(R.string.geocache_history))

        if ( geocachesFound.value != null) {
            Log.e("Geocache", geocachesFound.value!!.geocachesFound.isEmpty().toString())
            Log.e("Geocache", geocachesFound.value!!.geocachesFound.count().toString())

            if (geocachesFound.value!!.geocachesFound.isEmpty()) {
                Text(stringResource(R.string.no_found_geocaches))
            }

            Column {
                geocachesFound.value!!.geocachesFound.forEach { geocacheFound ->

                    val geocache =
                        geocacheViewsModels.getGeocache(
                            geocacheFound.challengedGeocacheId,
                        ).observeAsState()

                    if (geocache.value == null) {
                        Text(stringResource(R.string.error_try_later))
                    }

                    Spacer(Modifier.padding(15.dp))
                    geocache.value?.let {
                        GeocacheCard(
                            title = it.name,
                            description = geocache.value!!.address,
                            points = geocacheFound.points,
                            image = geocache.value!!.image!!
                        )
                    }
                }
            }
        } else {
            Text(stringResource(R.string.no_found_geocaches))
        }
    }
}

@Preview
@Composable
fun HistoryPreview() {
    val navController = rememberNavController()
    val userId = 1
    val avatar = ImageBitmap.imageResource(id = R.drawable.mercadona)

    val geocache1 =
        Geocache(
            1,
            Location(0.0, 0.0),
            GeocacheType.HISTORICO,
            "Name",
            "address",
            avatar,
            LocalDateTime.now(),
            userId
        )
    val geocache2 = Geocache(
        2,
        Location(0.0, 0.0),
        GeocacheType.HISTORICO,
        "Name",
        "address",
        avatar,
        LocalDateTime.now(),
        userId
    )
    val geocachesFound = listOf(
        geocache1,
        geocache2
    )

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                Column(Modifier.padding(28.dp)) {
                    Title(text = stringResource(R.string.geocache_history))

                    Column {
                        geocachesFound.forEach { geocache ->
                            Spacer(Modifier.padding(15.dp))
                            GeocacheCard(
                                title = geocache.name,
                                description = geocache.address,
                                points = 52, // valor fixo ou dinâmico
                                image = geocache.image!!
                            )
                        }
                    }
                }
            }
        }
    }
}