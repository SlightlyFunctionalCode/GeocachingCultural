package pt.ipp.estg.geocaching_cultural.database.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.ChallengedGeocache
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.dao.GeocacheDao
import pt.ipp.estg.geocaching_cultural.services.LocationUpdateService

class GeocacheRepository(val geocacheDao: GeocacheDao) {

    suspend fun insert(geocache: Geocache): Long {
        return geocacheDao.insertGeocache(geocache)
    }

    suspend fun update(geocache: Geocache) {
        geocacheDao.updateGeocache(geocache)
    }

    suspend fun delete(geocache: Geocache) {
        geocacheDao.deleteGeocache(geocache)
    }

    suspend fun insert(challenge: Challenge) {
        geocacheDao.insertChallenge(challenge)
    }

    suspend fun update(challenge: Challenge) {
        geocacheDao.updateChallenge(challenge)
    }

    suspend fun delete(challenge: Challenge) {
        geocacheDao.deleteChallenge(challenge)
    }

    suspend fun insert(hint: Hint) {
        geocacheDao.insertHint(hint)
    }

    suspend fun update(hint: Hint) {
        geocacheDao.updateHint(hint)
    }

    suspend fun delete(hint: Hint) {
        geocacheDao.deleteHint(hint)
    }

    fun getGeocache(geocacheId: Int): LiveData<Geocache> {
        return geocacheDao.getGeocache(geocacheId)
    }

    fun getChallengedGeocache(
        challengedGeocacheId: Int,
        userId: Int
    ): LiveData<ChallengedGeocache> {
        return geocacheDao.getChallengedGeocache(challengedGeocacheId, userId)
    }

    fun getGeocacheWithHintsAndChallenges(geocacheId: Int): LiveData<GeocacheWithHintsAndChallenges> {
        return geocacheDao.getGeocacheWithHintsAndChallenges(geocacheId)
    }

    fun getClosestGeocaches(userLocation: Location): LiveData<List<Pair<GeocacheWithHintsAndChallenges, Double>>> {
        val closestGeocaches = MutableLiveData<List<Pair<GeocacheWithHintsAndChallenges, Double>>>()

        // Get all geocaches from the database (this is LiveData)
        val allGeocaches = geocacheDao.getAllGeocacheWithHintsAndChallenges() // Fetch geocaches

        // Observe the LiveData of geocaches
        allGeocaches.observeForever { geocaches ->
            // Calculate distance for each geocache and filter out those farther than 10km
            val distances = geocaches.mapNotNull { geocache ->

                // Calculate the distance between user and geocache in meters
                val distance = LocationUpdateService.getDistanceToGeocache(
                    userLocation,
                    geocache.geocache.location
                )

                // Only include geocaches within 10 kilometers (10000 meters)
                if (distance <= 10000) {
                    Pair(geocache, distance)
                } else {
                    null // Exclude geocaches that are farther than 10 km
                }
            }

            // Sort by distance and take the top 5 closest geocaches
            val sortedGeocaches = distances.sortedBy { it.second }.take(5)

            // Set the value of the LiveData to the sorted list of closest geocaches
            closestGeocaches.value = sortedGeocaches
        }

        // Return the LiveData
        return closestGeocaches
    }

    fun getGeocachesByCategory(category: GeocacheType): LiveData<List<GeocacheWithHintsAndChallenges>> {
        return geocacheDao.getGeocachesByCategory(category)
    }
}