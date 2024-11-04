package pt.ipp.estg.geocashing_cultural.ui.classes.typeConverters

import androidx.room.TypeConverter

class HintConverter {
    @TypeConverter
    fun fromList(hints: List<String>): String = hints.joinToString(",")

    @TypeConverter
    fun toList(hints: String): List<String> = hints.split(",")
}