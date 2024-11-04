package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*
import pt.ipp.estg.geocashing_cultural.ui.classes.typeConverters.UserConverter

@Entity
data class Ranking(
    @PrimaryKey val rankingId: Int = 0,
    @TypeConverters(UserConverter::class) val ranking: List<User>
)