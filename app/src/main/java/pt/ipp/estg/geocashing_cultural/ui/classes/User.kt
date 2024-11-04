package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*

@Entity
data class User(
    @PrimaryKey val userId: Int,
    val name: String,
    val email: String,
    val password: String,
    val points: Int,
    val profileImageUrl: String?
)