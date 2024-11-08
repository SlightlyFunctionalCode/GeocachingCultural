package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

data class UserWithGeocachesFound(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "geocacheId",
        associateBy = Junction(UserGeocacheFoundCrossRef::class)
    )
    val geocachesFound: List<Geocache>
)