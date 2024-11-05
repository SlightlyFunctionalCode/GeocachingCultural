package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType
import pt.ipp.estg.geocaching_cultural.data.classes.typeConverters.ChallengeConverter
import pt.ipp.estg.geocaching_cultural.data.classes.typeConverters.HintConverter
import java.time.LocalDateTime

data class Geocache(
    @PrimaryKey val geocacheId: Int,
    @Embedded val location: Location,
    val type: GeocacheType,
    val name: String,
    val createdAt: LocalDateTime,
    val createdBy: Int,

    @TypeConverters(HintConverter::class)
    val hint: List<String>, // converter needed

    @TypeConverters(ChallengeConverter::class)
    val challenge: List<Challenge>
)