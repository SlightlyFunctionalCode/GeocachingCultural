package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

fun fetchAddressSuggestions(
    query: String,
    placesClient: PlacesClient,
    onSuggestionsFetched: (List<String>) -> Unit
) {
    if (query.isEmpty()) {
        onSuggestionsFetched(emptyList())
        return
    }

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setTypeFilter(TypeFilter.ADDRESS)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            val suggestions =
                response.autocompletePredictions.map { it.getFullText(null).toString() }
            onSuggestionsFetched(suggestions)
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Erro ao buscar sugestões: ${exception.message}")
            onSuggestionsFetched(emptyList())
        }
}

fun fetchPlaceSuggestions(
    query: String,
    placesClient: PlacesClient,
    onSuggestionsFetched: (List<AutocompletePrediction>) -> Unit
) {
    if (query.isEmpty()) {
        onSuggestionsFetched(emptyList())
        return
    }

    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .setTypeFilter(TypeFilter.ESTABLISHMENT)
        .build()

    placesClient.findAutocompletePredictions(request)
        .addOnSuccessListener { response ->
            onSuggestionsFetched(response.autocompletePredictions)
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Erro ao buscar sugestões: ${exception.message}")
            onSuggestionsFetched(emptyList())
        }
}

fun fetchPlaceDetails(
    placeId: String,
    placesClient: PlacesClient,
    onPlaceFetched: (Place) -> Unit
) {
    val request = FetchPlaceRequest.builder(
        placeId,
        listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
    ).build()

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            onPlaceFetched(response.place)
        }
        .addOnFailureListener { exception ->
            Log.e("Places", "Erro ao buscar detalhes do local: ${exception.message}")
        }
}