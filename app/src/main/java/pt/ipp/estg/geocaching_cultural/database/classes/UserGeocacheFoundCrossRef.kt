package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.Entity

@Entity(primaryKeys = ["userId", "geocacheId"])
data class UserGeocacheFoundCrossRef(
    val userId: Int,
    val geocacheId: Int
)