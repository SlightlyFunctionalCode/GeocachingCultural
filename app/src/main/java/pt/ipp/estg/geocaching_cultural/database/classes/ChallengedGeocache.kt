package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "challengedGeocacheId"])
data class ChallengedGeocache(
    val userId: Int,
    val challengedGeocacheId: Int,
    val points:Int
)