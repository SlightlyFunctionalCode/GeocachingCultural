package pt.ipp.estg.geocaching_cultural.utils_api

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache

@Composable
fun fetchGeocacheImage(geocache: Geocache, context: Context): Painter {
    // Estado para armazenar a imagem como ImageBitmap
    val imageState = remember { mutableStateOf<ImageBitmap?>(null) }
    val defaultImage = painterResource(R.drawable.home)

    val placesClient = Places.createClient(context)
    val placeFields = listOf(Place.Field.PHOTO_METADATAS)

    val context = LocalContext.current
    getApiKey(context)?.let { Places.initialize(context, it) }

    LaunchedEffect(geocache) {
        try {
            val apiKey = getApiKey(context)
            val placeId = getPlaceIdByCoordinatesAsync(geocache.location.lat, geocache.location.lng, apiKey)

            placeId?.let { id ->
                val request = FetchPlaceRequest.builder(id, placeFields).build()
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        val photoMetadata = response.place.photoMetadatas?.firstOrNull()

                        if (photoMetadata != null) {
                            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                .setMaxWidth(500)
                                .setMaxHeight(300)
                                .build()

                            placesClient.fetchPhoto(photoRequest)
                                .addOnSuccessListener { fetchPhotoResponse ->
                                    val bitmap = fetchPhotoResponse.bitmap
                                    imageState.value = bitmap.asImageBitmap()
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Places", "Erro ao buscar foto: ${exception.message}")
                                    imageState.value = null
                                }
                        } else {
                            Log.d("Places", "Metadados de foto nulos")
                            imageState.value = null
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Places", "Erro ao buscar informações do lugar: ${exception.message}")
                        imageState.value = null
                    }
            }
        } catch (e: Exception) {
            Log.e("Places", "Erro na requisição de rede: ${e.message}")
        }
    }

    return imageState.value?.let { BitmapPainter(it) } ?: defaultImage
}