package pt.ipp.estg.geocaching_cultural.retrofit

import pt.ipp.estg.geocaching_cultural.database.api_service.GeocodingApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val googlePlacesApi: GeocodingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApiService::class.java)
    }

}