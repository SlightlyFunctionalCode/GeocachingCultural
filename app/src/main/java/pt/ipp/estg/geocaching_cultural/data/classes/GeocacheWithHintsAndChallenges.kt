package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

class GeocacheWithHintsAndChallenges(
    @Embedded val geocache: Geocache,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "hintId"
    ) val hint: List<Hint>,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "challengeId"
    ) val challenges: List<Challenge>
)