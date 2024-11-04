package pt.ipp.estg.geocaching_cultural.data.classes.typeConverters

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType
import pt.ipp.estg.geocaching_cultural.data.classes.Geocache
import pt.ipp.estg.geocaching_cultural.data.classes.Location
import java.time.LocalDateTime

class GeocacheConverter {

    private val challengeConverter = ChallengeConverter()

    @TypeConverter
    fun fromGeocache(geocache: Geocache): String {
        // Serializamos os atributos do geocache para uma string, separando-os por delimitadores
        val challengesString = challengeConverter.fromChallengeList(geocache.challenge)
        val locationString = "${geocache.location.latitude},${geocache.location.longitude},${geocache.location.address ?: ""}"

        return "${geocache.geocacheId},${geocache.type},${geocache.name},$locationString,${geocache.hint.joinToString("|")},$challengesString,${geocache.createdBy},${geocache.createdAt}"
    }

    @SuppressLint("NewApi")
    @TypeConverter
    fun toGeocache(geocacheString: String): Geocache {
        val parts = geocacheString.split(";", limit = 9)

        // Extrair os valores dos campos de Geocache
        val geocacheId = parts[0].toInt()
        val type = GeocacheType.valueOf(parts[1])
        val name = parts[2]

        // Extrair a localização
        val locationParts = parts[3].split(";")
        val location = Location(
            latitude = locationParts[0].toDouble(),
            longitude = locationParts[1].toDouble(),
            address = if (locationParts.size > 2) locationParts[2] else null
        )

        // Extrair as dicas (hint)
        val hints = parts[4].split("|").toList()

        // Extrair os desafios
        val challenges = challengeConverter.toChallengeList(parts[5])
        val userId = parts[6].toInt()
        // Extrair o usuário que criou (apenas o ID, já que o objeto completo não está armazenado aqui)
        val createdBy = userId

        val createdAt = LocalDateTime.parse(parts[7])

        return Geocache(
            geocacheId = geocacheId,
            type = type,
            name = name,
            location = location,
            hint = hints,
            challenge = challenges,
            createdBy = createdBy,
            createdAt = createdAt
        )
    }
}