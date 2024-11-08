package pt.ipp.estg.geocaching_cultural.database.classes

import androidx.room.*

@Entity
data class  Hint(
    @PrimaryKey(autoGenerate = true) val hintId: Int,
    var geocacheId: Int,
    val hint: String
)
