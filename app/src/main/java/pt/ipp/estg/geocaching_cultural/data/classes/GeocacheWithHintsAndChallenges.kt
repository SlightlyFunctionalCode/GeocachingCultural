package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

class GeocacheWithHintsAndChallenges(
    @Embedded val geocache: Geocache,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "geocacheId"
    ) val hint: List<Hint>,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "geocacheId"
    ) val challenges: List<Challenge>
)