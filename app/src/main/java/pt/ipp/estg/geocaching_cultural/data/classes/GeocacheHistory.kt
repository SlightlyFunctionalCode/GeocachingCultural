package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.typeConverters.GeocacheConverter

@Entity(primaryKeys =["userId","geocacheId"])
data class GeocacheHistory(
    val userId:Int,
    val geocacheId: Int,
    @TypeConverters(GeocacheConverter::class) val foundGeocaches: List<Geocache>,
)