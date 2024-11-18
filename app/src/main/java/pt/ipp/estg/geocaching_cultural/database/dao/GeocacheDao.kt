package pt.ipp.estg.geocaching_cultural.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.ChallengedGeocache
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType

@Dao
interface GeocacheDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeocache(geocache: Geocache): Long

    @Update
    suspend fun updateGeocache(geocache: Geocache)

    @Delete
    suspend fun deleteGeocache(geocache: Geocache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Update
    suspend fun updateChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHint(hint: Hint)

    @Update
    suspend fun updateHint(hint: Hint)

    @Delete
    suspend fun deleteHint(hint: Hint)

    @Query(
        "SELECT * FROM Geocache G " +
                "WHERE G.geocacheId = :geocacheId"
    )
    fun getGeocache(geocacheId: Int): LiveData<Geocache>

    @Query(
        "SELECT * FROM ChallengedGeocache G " +
                "WHERE G.challengedGeocacheId = :challengedGeocacheId AND G.userId= :userId"
    )
    fun getChallengedGeocache(challengedGeocacheId: Int, userId: Int): LiveData<ChallengedGeocache>

    @Transaction
    @Query(
        "SELECT * FROM Geocache G " +
                "WHERE G.geocacheId = :geocacheId"
    )
    fun getGeocacheWithHintsAndChallenges(geocacheId: Int): LiveData<GeocacheWithHintsAndChallenges>

    @Transaction
    @Query("SELECT * FROM Geocache G ")
    fun getAllGeocacheWithHintsAndChallenges(): LiveData<List<GeocacheWithHintsAndChallenges>>

    @Transaction
    @Query(
        "SELECT * FROM Geocache G " +
                "WHERE G.type = :category"
    )
    fun getGeocachesByCategory(category: GeocacheType): LiveData<List<GeocacheWithHintsAndChallenges>>

}