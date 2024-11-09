package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

data class GeocacheWithHintsAndChallenges(
    @Embedded val geocache: Geocache,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "geocacheId"
    ) val hints: List<Hint>,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "geocacheId"
    ) val challenges: List<Challenge>
)