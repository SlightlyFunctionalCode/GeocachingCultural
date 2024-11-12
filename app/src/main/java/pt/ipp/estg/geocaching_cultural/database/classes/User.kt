package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val name: String,
    val email: String,
    val password: String,
    val points: Int,
    val profileImageUrl: String?,
    val profilePictureDefault: Int,
    val isWalking: Boolean = false,
    @Embedded val location: Location,
)