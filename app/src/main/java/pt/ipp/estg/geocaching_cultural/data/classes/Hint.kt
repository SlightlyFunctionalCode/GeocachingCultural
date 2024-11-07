package pt.ipp.estg.geocaching_cultural.data.classes

import androidx.room.*

@Entity
data class  Hint(
    @PrimaryKey(autoGenerate = true) val hintId: Int,
    val geocacheId: Int,
    val hint: String
)
