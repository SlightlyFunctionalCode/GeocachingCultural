package pt.ipp.estg.geocaching_cultural.database.classes

data class GeocodingResponse(
    val results: List<Result>
)

data class Result(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)
