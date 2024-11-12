package pt.ipp.estg.geocaching_cultural.database.api_service

import pt.ipp.estg.geocaching_cultural.database.classes.GeocodingResponse
import pt.ipp.estg.geocaching_cultural.database.classes.PlaceSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geocode/json")
    suspend fun getGeocodingData(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse

    @GET("place/nearbysearch/json")
    suspend fun getPlaceIdByCoordinates(
        @Query("location") location: String,
        @Query("radius") radius: Int = 100, // Ajuste o raio conforme necessário
        @Query("key") apiKey: String
    ): Response<PlaceSearchResponse>

    @GET("place/findplacefromtext/json")
    suspend fun getPlaceIdByAddress(
        @Query("input") address: String,
        @Query("inputtype") inputType: String = "textquery",
        @Query("fields") fields: String = "place_id",
        @Query("key") apiKey: String
    ): Response<PlaceSearchResponse>
}
