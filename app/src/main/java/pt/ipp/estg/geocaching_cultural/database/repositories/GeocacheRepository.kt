package pt.ipp.estg.geocaching_cultural.database.repositories

import androidx.lifecycle.LiveData
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.dao.GeocacheDao

class GeocacheRepository(val geocacheDao : GeocacheDao)  {

    suspend fun insert(geocache: Geocache){
        geocacheDao.insertGeocache(geocache)
    }

    suspend fun update(geocache: Geocache){
        geocacheDao.updateGeocache(geocache)
    }

    suspend fun delete(geocache: Geocache){
        geocacheDao.deleteGeocache(geocache)
    }

    suspend fun insert(challenge: Challenge){
        geocacheDao.insertChallenge(challenge)
    }

    suspend fun update(challenge: Challenge){
        geocacheDao.updateChallenge(challenge)
    }

    suspend fun delete(challenge: Challenge){
        geocacheDao.deleteChallenge(challenge)
    }

    suspend fun insert(hint : Hint){
        geocacheDao.insertHint(hint)
    }

    suspend fun update(hint : Hint){
        geocacheDao.updateHint(hint)
    }

    suspend fun delete(hint : Hint){
        geocacheDao.deleteHint(hint)
    }

    fun getGeocache(geocacheId : Int) {
        geocacheDao.getGeocache(geocacheId)
    }

    fun getGeocacheWithHintsAndChallenges(geocacheId : Int) {
        geocacheDao.getGeocacheWithHintsAndChallenges(geocacheId)
    }
}