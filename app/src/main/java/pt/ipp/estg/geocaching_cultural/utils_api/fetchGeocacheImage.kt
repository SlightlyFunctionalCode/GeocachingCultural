package pt.ipp.estg.geocaching_cultural.utils_api

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache

// Função `suspend` para buscar a imagem de um geocache
suspend fun fetchGeocacheImageAsync(geocache: Geocache, context: Context): ImageBitmap? {
    val apiKey = getApiKey(context)
    val placesClient = Places.createClient(context)
    val placeFields = listOf(Place.Field.PHOTO_METADATAS)

    return try {
        val placeId = getPlaceIdByCoordinatesAsync(geocache.location.lat, geocache.location.lng, apiKey)

        placeId?.let { id ->
            val request = FetchPlaceRequest.builder(id, placeFields).build()
            val response = placesClient.fetchPlace(request).await() // usando await para chamadas suspensas

            val photoMetadata = response.place.photoMetadatas?.firstOrNull()
            if (photoMetadata != null) {
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(300)
                    .build()

                val photoResponse = placesClient.fetchPhoto(photoRequest).await()
                photoResponse.bitmap.asImageBitmap() // Retorna o ImageBitmap
            } else {
                null // Retorna null se não houver metadados de foto
            }
        }
    } catch (e: Exception) {
        Log.e("Places", "Erro na requisição de rede: ${e.message}")
        null // Retorna null em caso de erro
    }
}

suspend fun fetchNearbyImages(
    latitude: Double,
    longitude: Double,
    apiKey: String,
    images: SnapshotStateList<ImageBitmap>,
    context: Context
) {
    withContext(Dispatchers.IO) {
        try {
            val placeIds = getNearbyPlaceIdsAsync(latitude, longitude, apiKey)
            val placesClient = Places.createClient(context)

            placeIds.forEach { placeId ->
                val placeFields = listOf(Place.Field.PHOTO_METADATAS)
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                val response = placesClient.fetchPlace(request).await()
                val photoMetadata = response.place.photoMetadatas?.firstOrNull()

                if (photoMetadata != null) {
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500)
                        .setMaxHeight(300)
                        .build()
                    val photoResponse = placesClient.fetchPhoto(photoRequest).await()
                    images.add(photoResponse.bitmap.asImageBitmap())
                }
            }
        } catch (e: Exception) {
            Log.e("Places", "Erro ao buscar imagens próximas: ${e.message}")
        }
    }
}

// Função `@Composable` que gerencia o estado da imagem e invoca `fetchGeocacheImageAsync`
@Composable
fun fetchGeocacheImage(geocache: Geocache, context: Context): Painter {
    val imageState = remember { mutableStateOf<ImageBitmap?>(null) }
    val defaultImage = painterResource(R.drawable.home)

    LaunchedEffect(geocache) {
        // Chama a função suspensa e atualiza o estado da imagem
        imageState.value = fetchGeocacheImageAsync(geocache, context)
    }

    return imageState.value?.let { BitmapPainter(it) } ?: defaultImage
}

@Composable
fun fetchGeocachesImages(latitude: Double, longitude: Double, context: Context): List<ImageBitmap> {
    // Lista para armazenar as imagens
    val images = remember { mutableStateListOf<ImageBitmap>() }

    LaunchedEffect(latitude, longitude) {
        // A função suspensa é chamada dentro do LaunchedEffect
        fetchNearbyImages(
            latitude = latitude,
            longitude = longitude,
            apiKey = getApiKey(context) ?: "",
            images = images,
            context = context
        )
    }

    // Se a lista de imagens estiver vazia, use uma imagem padrão
    return images
}