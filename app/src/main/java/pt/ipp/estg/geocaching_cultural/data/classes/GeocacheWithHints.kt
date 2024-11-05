package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

data class GeocacheWithHints(
    @Embedded val geocache: Geocache,
    @Relation(
        parentColumn = "geocacheId",
        entityColumn = "hintId"
    )
    val hints: List<Hint>
)
