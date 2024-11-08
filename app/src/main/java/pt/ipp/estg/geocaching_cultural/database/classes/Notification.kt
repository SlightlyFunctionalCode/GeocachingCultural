package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

@Entity
data class Notification(
    @PrimaryKey(autoGenerate = true) val notificationId: Int,
    val message: String,
    val title: String,
    val isRead: Boolean
)