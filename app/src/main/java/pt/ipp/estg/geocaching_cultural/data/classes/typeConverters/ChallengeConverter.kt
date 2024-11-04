package pt.ipp.estg.geocaching_cultural.data.classes.typeConverters

import androidx.room.TypeConverter
import pt.ipp.estg.geocaching_cultural.data.classes.Challenge

class ChallengeConverter {

    @TypeConverter
    fun fromChallengeList(challenges: List<Challenge>): String {
        // Converte a lista para uma string no formato JSON-like simples
        return challenges.joinToString(";") { "${it.challengeId},${it.question},${it.correctAnswer}" }
    }

    @TypeConverter
    fun toChallengeList(challengeString: String): List<Challenge> {
        // Divide a string de volta para uma lista de desafios
        return if (challengeString.isEmpty()) {
            emptyList()
        } else {
            challengeString.split(";").map {
                val parts = it.split(",")
                Challenge(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]))
            }
        }
    }
}
