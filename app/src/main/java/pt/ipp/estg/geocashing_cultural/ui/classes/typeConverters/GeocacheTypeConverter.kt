package pt.ipp.estg.geocashing_cultural.ui.classes.typeConverters

import androidx.room.TypeConverter
import pt.ipp.estg.geocashing_cultural.ui.classes.Enum.GeocacheType

class GeocacheTypeConverter {
    @TypeConverter
    fun fromGeocacheType(type: GeocacheType): String = type.name

    @TypeConverter
    fun toGeocacheType(type: String): GeocacheType = GeocacheType.valueOf(type)
}