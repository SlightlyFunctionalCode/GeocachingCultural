package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*

@Entity
data class Challenge(
    @PrimaryKey val challengeId: String,
    val question: String,
    val correctAnswer: String
)