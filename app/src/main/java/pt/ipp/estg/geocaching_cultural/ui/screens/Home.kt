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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.services.EnableLocation
import pt.ipp.estg.geocaching_cultural.services.LocationUpdateService
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.CloseGeocache
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels,
    usersViewsModels: UsersViewsModels
) {
    val context = LocalContext.current

    EnableLocation(
        context = context,
        locationUpdateService = usersViewsModels.locationUpdateService,
        onSuccess = {
            // Start location updates after enabling services
            if (!usersViewsModels.isUpdatingLocation) {
                usersViewsModels.startLocationUpdates(context)
            }
        }
    )

    LaunchedEffect(usersViewsModels.isUpdatingLocation) {
        usersViewsModels.startLocationUpdates(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            usersViewsModels.stopLocationUpdates()
        }
    }

    val currentUser = usersViewsModels.currentUser.observeAsState()

    val address = if (currentUser.value != null && currentUser.value?.location != null) {
        val addressLiveData = LocationUpdateService.getAddressFromCoordinates(
            context,
            currentUser.value!!.location
        )

        val addressState = addressLiveData.observeAsState()

        addressState.value ?: ""
    } else {
        ""
    }

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
            if (currentUser.value != null) {
                Text(
                    text = address,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.closest_geocaches),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(stringResource(R.string.error_try_later))
            }
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
            Text(stringResource(R.string.no_geocaches_in_10km))
            SmallVerticalSpacer()
        } else {
            for (geocacheAndDistance in closest5Geocaches) {

                CloseGeocache(
                    hint = if (geocacheAndDistance.first.hints.isNotEmpty()) geocacheAndDistance.first.hints[0].hint else stringResource(
                        R.string.no_hint
                    ),

                    distance = "${
                        String.format(
                            Locale.getDefault(),
                            "%.1f",
                            geocacheAndDistance.second / 1000
                        )
                    } Km",
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
            stringResource(R.string.more_geocaches), onClick = {
                navController.navigate("explorarScreen/${GeocacheType.HISTORICO.name}")
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

    val userId = 1
    val avatar = ImageBitmap.imageResource(id = R.drawable.avatar_male_01)
    val address = "Porto, Portugal"

    val geocache1 =
        Geocache(
            1,
            Location(0.0, 0.0),
            GeocacheType.HISTORICO,
            "Address 1",
            "ImageURL1",
            avatar,
            LocalDateTime.now(),
            userId
        )
    val geocache2 = Geocache(
        2,
        Location(0.0, 0.0),
        GeocacheType.HISTORICO,
        "Address 1",
        "ImageURL1",
        avatar,
        LocalDateTime.now(),
        userId
    )

    val hint1 = Hint(1, 1, "Description 1")
    val hint2 = Hint(2, 1, "Description 2")

    val challenge1 = Challenge(1, 1, "Question1", "Answer1", 100)
    val challenge2 = Challenge(2, 1, "Question2", "Answer2", 100)

    val geocacheWithHintsAndChallenges1 = GeocacheWithHintsAndChallenges(
        geocache1,
        listOf(hint1, hint2),
        listOf(challenge1, challenge2)
    )

    val geocacheWithHintsAndChallenges2 = GeocacheWithHintsAndChallenges(
        geocache2,
        listOf(hint1, hint2),
        listOf(challenge1, challenge2)
    )

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {

            item {
                val closeGeocachesWithHints = listOf(
                    Pair(geocacheWithHintsAndChallenges1, 12.0),
                    Pair(geocacheWithHintsAndChallenges2, 10.0)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(30.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = address,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = stringResource(R.string.closest_geocaches),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    VerticalSpacer()

                    Closest5Geocaches(closeGeocachesWithHints, navController)
                }
            }
        }
    }
}

