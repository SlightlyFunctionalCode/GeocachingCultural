package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

data class GeocacheWithChallenges(
    @Embedded val geocache: Geocache,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "challengeId"
    ) val challenges: List<Challenge>
)
