package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.GeocacheCard
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer
import java.time.LocalDateTime

@Composable
fun CreatedGeocachesScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val userId by usersViewsModels.currentUserId.observeAsState()
    val geocachesCreated =
        usersViewsModels.getUserWithGeocachesCreated(userId!!).observeAsState().value

    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
        Title(text = stringResource(R.string.created_geocaches))

        if (geocachesCreated != null) {
            if(geocachesCreated.geocachesCreated.isEmpty()){
                Text(stringResource(R.string.no_created_geocaches))
            }
            Column {
                geocachesCreated.geocachesCreated.forEach { geocache ->
                    VerticalSpacer()

                    GeocacheCard(
                        title = geocache.name,
                        description = geocache.address,
                        image = geocache.image!!,
                        modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen/${geocache.geocacheId}") }
                    )
                }
            }
        }else{
            Text(stringResource(R.string.no_created_geocaches))
        }
    }
}

@Preview
@Composable
fun CreatedGeocachesPreview() {
    val navController = rememberNavController()
    val userId = 1
    val avatar = ImageBitmap.imageResource(id = R.drawable.mercadona)
    val geocache1 =
        Geocache(
            1,
            Location(0.0, 0.0),
            GeocacheType.HISTORICO,
            "name",
            "Address 1",
            avatar,
            LocalDateTime.now(),
            userId
        )
    val geocache2 = Geocache(
        2,
        Location(0.0, 0.0),
        GeocacheType.HISTORICO,
        "name",
        "Address 1",
        avatar,
        LocalDateTime.now(),
        userId
    )

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                val geocachesCreated = listOf(
                    geocache1,
                    geocache2
                )
                Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
                    Title(text = stringResource(R.string.created_geocaches))

                    Column {
                        geocachesCreated.forEach { geocache ->
                            VerticalSpacer()

                            GeocacheCard(
                                title = geocache.name,
                                description = geocache.address,
                                image = geocache.image!!,
                                modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen/${geocache.geocacheId}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

