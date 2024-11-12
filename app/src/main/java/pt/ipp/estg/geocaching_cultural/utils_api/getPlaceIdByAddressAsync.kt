package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.retrofit.RetrofitInstance
import java.net.URLEncoder

suspend fun getPlaceIdByAddressAsync(
    address: String,
    apiKey: String?
): String? {
    return withContext(Dispatchers.IO) {
        try {
            val response = RetrofitInstance.googlePlacesApi.getPlaceIdByAddress(
                address = URLEncoder.encode(address, "UTF-8"),
                apiKey = apiKey!!
            )

            if (response.isSuccessful) {
                response.body()?.results?.firstOrNull()?.place_id
            } else {
                Log.e("Places", "Erro na resposta: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Places", "Erro na requisição de endereço: ${e.message}")
            null
        }
    }
}