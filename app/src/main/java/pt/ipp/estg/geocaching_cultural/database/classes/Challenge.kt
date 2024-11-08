package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true) val challengeId: Int,
    var geocacheId: Int,
    var question: String,
    var correctAnswer: String,
    val pointsAwarded: Int
)