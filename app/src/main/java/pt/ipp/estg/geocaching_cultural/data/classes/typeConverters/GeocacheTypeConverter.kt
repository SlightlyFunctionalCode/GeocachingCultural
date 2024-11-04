package pt.ipp.estg.geocaching_cultural.data.classes.typeConverters

import androidx.room.TypeConverter
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType

class GeocacheTypeConverter {
    @TypeConverter
    fun fromGeocacheType(type: GeocacheType): String = type.name

    @TypeConverter
    fun toGeocacheType(type: String): GeocacheType = GeocacheType.valueOf(type)
}