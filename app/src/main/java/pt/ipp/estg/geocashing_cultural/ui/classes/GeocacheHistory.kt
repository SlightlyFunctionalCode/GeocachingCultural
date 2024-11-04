package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*
import pt.ipp.estg.geocashing_cultural.ui.classes.typeConverters.GeocacheConverter

@Entity
data class GeocacheHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val user: Int, // FK referencing User
    @TypeConverters(GeocacheConverter::class) val foundGeocaches: List<Geocache>, // converter needed
    val totalPoints: Int
)