package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import java.time.LocalDateTime

@Composable
fun GeocacheFoundScreen(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    val userId by usersViewsModels.currentUserId.observeAsState()
    val geocacheId = 2 // Esse ID deve vir de uma fonte dinâmica em vez de ser estático
    val geocacheFound = UserGeocacheFoundCrossRef(userId!!, 1)
    val geocache = geocacheViewsModels.getGeocache(geocacheFound.geocacheId).observeAsState().value


    if (geocache != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFCC00)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.geocache_found),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Imagem do Geocache
                    Image(
                        painter = BitmapPainter(geocache.image!!),
                        contentDescription = stringResource(R.string.geocache_image),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nome e localização do local
                    Text(
                        text = geocache.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.congratulations),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Ganhou 5 pontos",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            usersViewsModels.insertUserGeocacheFound(
                                UserGeocacheFoundCrossRef(
                                    userId!!,
                                    geocacheId
                                )
                            )
                            navController.navigate("homeScreen")
                        },
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text(
                            text = stringResource(R.string.home),
                            color = Color.White
                        )
                    }
                }
                GeocacheMap(geocache)
            }
        }
    } else {
        Text(text = stringResource(R.string.error_try_later))
    }
}

@Composable
fun GeocacheMap(geocache: Geocache) {
    // Coordinates for Mercadona in Felgueiras
    val location = LatLng(geocache.location.lat, geocache.location.lng)
    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 17f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        // Marker on the map
        Marker(
            state = rememberMarkerState(position = location),
            title = "Mercadona",
            snippet = "Felgueiras, Porto"
        )
    }
}

@Preview
@Composable
fun GeocacheFoundScreenPreview() {
    val navController = rememberNavController()
    val userId = 1
    val avatar = ImageBitmap.imageResource(id = R.drawable.mercadona)
    val map = ImageBitmap.imageResource(id = R.drawable.mapa_localizacao)
    val geocache =
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFCC00)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.geocache_found),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Imagem do Geocache
                Image(
                    painter = BitmapPainter(geocache.image!!),
                    contentDescription = stringResource(R.string.geocache_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nome e localização do local
                Text(
                    text = geocache.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.congratulations),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF388E3C),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Ganhou 5 pontos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                    },
                    colors = ButtonDefaults.buttonColors(Color.Blue)
                ) {
                    Text(
                        text = stringResource(R.string.home),
                        color = Color.White
                    )
                }
            }
            Image(
                painter = BitmapPainter(map),
                contentDescription =  stringResource(R.string.geocache_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}