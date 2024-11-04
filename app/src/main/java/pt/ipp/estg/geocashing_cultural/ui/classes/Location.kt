package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*


@Entity
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String?
)