package pt.ipp.estg.geocaching_cultural.data.classes.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromTimestamp(timestamp: Long?): LocalDateTime? {
        return timestamp?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toEpochSecond(ZoneOffset.UTC)
    }
}