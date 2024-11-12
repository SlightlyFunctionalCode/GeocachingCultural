package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.retrofit.RetrofitInstance

suspend fun getPlaceIdByCoordinatesAsync(
    latitude: Double,
    longitude: Double,
    apiKey: String?
): String? {
    return withContext(Dispatchers.IO) {
        try {
            val location = "$latitude,$longitude"
            val response = RetrofitInstance.googlePlacesApi.getPlaceIdByCoordinates(location, apiKey = apiKey!!)

            if (response.isSuccessful) {
                Log.d("Places", "Resposta da API: ${response.body()}")
                response.body()?.results?.firstOrNull()?.place_id
            } else {
                Log.e("Places", "Erro na resposta: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Places", "Erro na requisição de coordenadas: ${e.message}")
            null
        }
    }
}