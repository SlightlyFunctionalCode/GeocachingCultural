package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.retrofit.RetrofitInstance
import java.net.URLEncoder

// Função para buscar coordenadas a partir de um endereço
suspend fun fetchCoordinatesFromAddress(
    address: String,
    apiKey: String
): Pair<Double?, Double?>? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.googlePlacesApi.getGeocodingData(
                address = URLEncoder.encode(address, "UTF-8"),
                apiKey = apiKey
            )

            response.results.firstOrNull()?.geometry?.location?.let { location ->
                Log.d(
                    "Geocoding",
                    "Coordenadas encontradas: Latitude = ${location.lat}, Longitude = ${location.lng}"
                )
                Pair(location.lat, location.lng)
            }
        } catch (e: Exception) {
            Log.e("Geocoding", "Erro na requisição de geocodificação: ${e.message}")
            null
        }
    }
}