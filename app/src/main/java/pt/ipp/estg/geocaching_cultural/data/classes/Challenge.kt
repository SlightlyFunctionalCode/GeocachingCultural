package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

@Entity
data class Challenge(
    @PrimaryKey val challengeId: String,
    val question: String,
    val correctAnswer: String,
    val pointsAwarded: Int,
)