package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

@Entity
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String?
)