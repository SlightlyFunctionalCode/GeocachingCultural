package pt.ipp.estg.geocaching_cultural.database.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.repositories.GeocacheRepository
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository
import pt.ipp.estg.geocaching_cultural.services.LocationUpdateService

class GeocacheViewsModels(application: Application) : AndroidViewModel(application) {
    private val repository: GeocacheRepository

    init {
        val db = AppDatabase.getDatabase(application)
        //val restAPI = RetrofitHelper.getInstance().create(BookAPI::class.java)
        repository = GeocacheRepository(db.GeocacheDao()/*restAPI*/)
        //allBooks = repository.getBooks()
        //this.updateBooksOnline()
    }

    fun insertGeocache(geocache: Geocache): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            val geocacheId = repository.insert(geocache)
            result.postValue(geocacheId)
        }
        return result
    }

    fun updateGeocache(geocache: Geocache) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(geocache)
        }
    }

    fun deleteGeocache(geocache: Geocache) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(geocache)
        }
    }

    fun insertChallenge(challenge: Challenge) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(challenge)
        }
    }

    fun updateChallenge(challenge: Challenge) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(challenge)
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(challenge)
        }
    }

    fun insertHint(hint: Hint) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(hint)
        }
    }

    fun updateHint(hint: Hint) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(hint)
        }
    }

    fun deleteHint(hint: Hint) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(hint)
        }
    }

    fun getGeocache(geocacheId: Int): LiveData<Geocache> {
        return repository.getGeocache(geocacheId)
    }

    fun getGeocacheWithHintsAndChallenges(geocacheId: Int): LiveData<GeocacheWithHintsAndChallenges> {
        return repository.getGeocacheWithHintsAndChallenges(geocacheId)
    }


    fun getClosestGeocaches(userLocation: Location): LiveData<List<Pair<GeocacheWithHintsAndChallenges, Double>>> {
        return repository.getClosestGeocaches(userLocation)
    }


    fun getGeocachesByCategory(category: GeocacheType): LiveData<List<GeocacheWithHintsAndChallenges>> {
        return repository.getGeocachesByCategory(category)
    }
}