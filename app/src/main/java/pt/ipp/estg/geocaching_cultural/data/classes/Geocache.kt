package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType
import java.time.LocalDateTime

@Entity
data class Geocache(
    @PrimaryKey(autoGenerate = true) val geocacheId: Int,
    @Embedded val location: Location,
    val type: GeocacheType,
    val name: String,
    val createdAt: LocalDateTime,
    val createdBy: Int,
)

