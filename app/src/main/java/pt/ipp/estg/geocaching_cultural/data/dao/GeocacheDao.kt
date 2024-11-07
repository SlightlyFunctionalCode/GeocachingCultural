package pt.ipp.estg.geocaching_cultural.data.dao

import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.Challenge
import pt.ipp.estg.geocaching_cultural.data.classes.Geocache
import pt.ipp.estg.geocaching_cultural.data.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.data.classes.Hint

@Dao
interface GeocacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGeocache(geocache: Geocache)

    @Update
    fun updateGeocache(geocache: Geocache)

    @Delete
    fun deleteGeocache(geocache: Geocache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChallenge(challenge: Challenge)

    @Update
    fun updateChallenge(challenge: Challenge)

    @Delete
    fun deleteGeocache(challenge: Challenge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHint(hint: Hint)

    @Update
    fun updateHint(hint: Hint)

    @Delete
    fun deleteHint(hint: Hint)

    @Query("SELECT * FROM Geocache G " +
            "WHERE G.geocacheId = :geocacheId")
    fun getGeocache(geocacheId: Int): Geocache

    @Query("SELECT * FROM Geocache G " +
            "WHERE G.geocacheId = :geocacheId")
    fun getGeocacheWithHintsAndChallenges(geocacheId: Int): GeocacheWithHintsAndChallenges

}