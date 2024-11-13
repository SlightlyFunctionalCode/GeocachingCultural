package pt.ipp.estg.geocaching_cultural.database.classes

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.room.*
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import java.time.LocalDateTime

@Entity
data class Geocache(
    @PrimaryKey(autoGenerate = true) val geocacheId: Int,
    @Embedded val location: Location,
    val type: GeocacheType,
    val name: String,
    val address: String,
    val image: ImageBitmap?,
    val createdAt: LocalDateTime,
    val createdBy: Int,
){
    override fun toString(): String {
        return "Geocache(id=$geocacheId, name=$name, type=$type, createdAt=$createdAt, createdBy=$createdBy)"
    }
}

