package pt.ipp.estg.geocaching_cultural.utils_api

import android.util.Log
import com.google.android.libraries.places.api.model.TypeFilter
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