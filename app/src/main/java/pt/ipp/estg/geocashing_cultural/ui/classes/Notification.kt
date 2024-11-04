package pt.ipp.estg.geocashing_cultural.ui.classes


import androidx.room.*

@Entity
data class Notification(
    @PrimaryKey val notificationId: Int,
    val message: String,
    val title: String,
    val isRead: Boolean
)