package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*

@Entity
data class Geocache(
    @PrimaryKey val geocacheId: Int,
    @Embedded val location: Location,
    val type: GeocacheType,
    val name: String,
    val pointsAwarded: Int,
    val createdAt: String, // use appropriate date type
    val createdBy: Int, // FK referencing User
    @TypeConverters(HintConverter::class) val hint: List<String>, // converter needed
    @TypeConverters(ChallengeConverter::class) val challenge: List<Challenge>
)