package pt.ipp.estg.geocaching_cultural.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.utils_api.fetchGeocacheImage
import pt.ipp.estg.geocaching_cultural.utils_api.getApiKey
import java.time.LocalDateTime

@Composable
fun HistoryScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {

    val user by usersViewsModels.currentUserId.observeAsState()

    val geocacheFound = UserGeocacheFoundCrossRef(user!!, 1)
    usersViewsModels.insertUserGeocacheFound(geocacheFound) /* TODO: isto deve ser feito quando um geocache é encontrado quando estiver implementado retirar daqui*/

    val geocachesFound =
        user?.let { usersViewsModels.getUserWithGeocachesFound(it).observeAsState() }?.value

    Column(Modifier.padding(28.dp)) {
        Title(text = stringResource(R.string.geocache_history))

        if (geocachesFound != null) {
            if(geocachesFound.geocachesFound.isEmpty()){
                Text(stringResource(R.string.no_found_geocaches))
            }

            Column {
                geocachesFound.geocachesFound.forEach { geocache ->
                    Spacer(Modifier.padding(15.dp))
                    GeocacheCard(
                        title = geocache.name,
                        description = geocache.address,
                        points = 52, // valor fixo ou dinâmico
                        image = geocache.image!!
                    )
                }
            }
        } else{
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