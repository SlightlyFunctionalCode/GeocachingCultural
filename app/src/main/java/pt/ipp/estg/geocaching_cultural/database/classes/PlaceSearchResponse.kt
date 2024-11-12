package pt.ipp.estg.geocaching_cultural.database.classes

data class PlaceSearchResponse (
    val results: List<PlaceResult>
)

data class PlaceResult(
    val place_id: String // apenas o place_id
)
