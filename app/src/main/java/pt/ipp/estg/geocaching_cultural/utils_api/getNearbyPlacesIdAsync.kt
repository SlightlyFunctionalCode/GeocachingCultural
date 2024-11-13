package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.retrofit.RetrofitInstance

suspend fun getNearbyPlaceIdsAsync(
    latitude: Double,
    longitude: Double,
    apiKey: String?
): List<String> {
    return withContext(Dispatchers.IO) {
        try {
            val location = "$latitude,$longitude"
            val response = RetrofitInstance.googlePlacesApi.getNearbyPlaces(location, apiKey = apiKey!!)

            if (response.isSuccessful) {
                Log.d("Places", "Resposta da API: ${response.body()}")
                // Retorna uma lista de `place_id`s dos locais próximos
                response.body()?.results?.map { it.place_id } ?: emptyList()
            } else {
                Log.e("Places", "Erro na resposta: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Places", "Erro na requisição de coordenadas: ${e.message}")
            emptyList()
        }
    }
}