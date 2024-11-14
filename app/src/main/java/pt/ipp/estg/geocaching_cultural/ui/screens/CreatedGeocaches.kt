package pt.ipp.estg.geocaching_cultural.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.White
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
        Title(text = "Geocaches Criados")

        if (geocachesCreated != null) {
            Column {
                geocachesCreated.geocachesCreated.forEach { geocache ->
                    VerticalSpacer()

                    GeocacheCard(
                        title = geocache.name,
                        description = geocache.address,
                        image = geocache.image!!, // Usa o estado da imagem atualizado
                        modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen/${geocache.geocacheId}") }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CreatedGeocachesPreview() {
    val navController = rememberNavController()
    // Simulação dos dados do ViewModel diretamente dentro do Preview
    val currentUserId = MutableLiveData(1)
    val userId = 1
    var avatar = ImageBitmap.imageResource(id = R.drawable.avatar_male_01)
    val user = User(
        1,
        name = "admin",
        email = "admin@ad.ad",
        password = "admin123",
        points = 100,
        profileImageUrl = "avatar",
        profilePictureDefault = 1,
        isWalking = true,
        location = Location(0.0, 0.0),
    )
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

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                    val geocachesCreated = listOf(geocache1,
                            geocache2)
                    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {
                        Title(text = "Geocaches Criados")

                        if (geocachesCreated != null) {
                            Column {
                                geocachesCreated.forEach { geocache ->
                                    VerticalSpacer()

                                    GeocacheCard(
                                        title = geocache.name,
                                        description = geocache.address,
                                        image = geocache.image!!, // Usa o estado da imagem atualizado
                                        modifier = Modifier.clickable { navController.navigate("createdGeocacheDetailsScreen/${geocache.geocacheId}") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
