package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

data class UserWithGeocachesCreated(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "createdBy"
    ) val geocachesCreated: List<Geocache>
)
