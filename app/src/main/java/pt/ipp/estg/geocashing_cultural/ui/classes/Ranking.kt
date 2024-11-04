package pt.ipp.estg.geocashing_cultural.ui.classes

import androidx.room.*

@Entity
data class Ranking(
    @PrimaryKey val rankingId: Int = 0,
    @TypeConverters(UserConverter::class) val ranking: List<User>
)