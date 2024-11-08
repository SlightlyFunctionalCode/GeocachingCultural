package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true) val challengeId: Int,
    val geocacheId: Int,
    val question: String,
    val correctAnswer: String,
    val pointsAwarded: Int
)