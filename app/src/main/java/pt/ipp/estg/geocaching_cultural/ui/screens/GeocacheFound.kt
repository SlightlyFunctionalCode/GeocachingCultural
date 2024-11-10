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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import pt.ipp.estg.geocaching_cultural.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels

@Composable
fun GeocacheFoundScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val user by usersViewsModels.currentUserId.observeAsState()

    val geocacheFound = UserGeocacheFoundCrossRef(user!!, 2) /* TODO: este geocache id deve de se ir buscar a outro lado n deve ser estático  */
    /* TODO: ainda é preciso nesta screen alterar os daodos estáticos para ir buscar os corretos a partir de uma api*/
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFCC00)), // Fundo amarelo
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
                    text = "Geocache Encontrado",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Imagem do Geocache
                Image(
                    painter = painterResource(id = R.drawable.mercadona),
                    contentDescription = "Imagem do Local",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nome e localização do local
                Text(
                    text = "Mercadona - Felgueiras, Porto",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mensagem de parabéns
                Text(
                    text = "Parabéns!!!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF388E3C), // Cor verde para destaque
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                // Pontuação ganha
                Text(
                    text = "Ganhou 5 pontos",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão de Direções
                Button(
                    onClick = {
                        usersViewsModels.insertUserGeocacheFound(geocacheFound)
                        navController.navigate("homeScreen")
                              },
                    colors = ButtonDefaults.buttonColors(Color.Blue)
                ) {
                    Text(
                        text = "Home",
                        color = Color.White
                    )
                }
            }
            GeocacheMap()
        }
    }
}

@Composable
fun GeocacheMap() {
    // Coordinates for Mercadona in Felgueiras
    val location = LatLng(41.35898846349719, -8.193187783713917)
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
    //GeocacheFoundScreen(navController)
}