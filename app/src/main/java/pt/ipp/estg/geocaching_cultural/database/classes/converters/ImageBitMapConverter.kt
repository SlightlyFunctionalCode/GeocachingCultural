package pt.ipp.estg.geocaching_cultural.database.classes.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class ImageBitMapConverter {

    @TypeConverter
    fun fromImageBitmap(imageBitmap: ImageBitmap?): ByteArray? {
        return imageBitmap?.let {
            // Converte ImageBitmap para Bitmap
            val bitmap = it.asAndroidBitmap()

            // Converte Bitmap para ByteArray
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        }
    }

    @TypeConverter
    fun toImageBitmap(byteArray: ByteArray?): ImageBitmap? {
        return byteArray?.let {
            // Converte ByteArray para Bitmap
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)

            // Converte Bitmap para ImageBitmap
            bitmap.asImageBitmap()
        }
    }
}