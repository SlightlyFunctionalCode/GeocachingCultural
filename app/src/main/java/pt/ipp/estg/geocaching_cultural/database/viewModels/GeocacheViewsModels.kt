package pt.ipp.estg.geocaching_cultural.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.repositories.GeocacheRepository
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository

class GeocacheViewsModels (application : Application) : AndroidViewModel(application){
    val repository : GeocacheRepository

    init {
        val db = AppDatabase.getDatabase(application)
        //val restAPI = RetrofitHelper.getInstance().create(BookAPI::class.java)
        repository = GeocacheRepository(db.GeocacheDao(),/*restAPI*/)
        //allBooks = repository.getBooks()
        //this.updateBooksOnline()
    }

    fun insertGeocache(geocache : Geocache){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(geocache)
        }
    }

    fun updateGeocache(geocache : Geocache){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(geocache)
        }
    }

    fun deleteGeocache(geocache : Geocache){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(geocache)
        }
    }

    fun insertChallenge(challenge: Challenge){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(challenge)
        }
    }

    fun updateChallenge(challenge: Challenge){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(challenge)
        }
    }

    fun deleteChallenge(challenge: Challenge){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(challenge)
        }
    }

    fun insertHint(hint : Hint){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(hint)
        }
    }

    fun updateHint(hint : Hint){
        viewModelScope.launch(Dispatchers.IO){
            repository.update(hint)
        }
    }

    fun deleteHint(hint : Hint){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(hint)
        }
    }

    fun getGeocache(geocacheId : Int) {
        return repository.getGeocache(geocacheId)
    }

    fun getGeocacheWithHintsAndChallenges(geocacheId : Int) {
        return repository.getGeocacheWithHintsAndChallenges(geocacheId)
    }

    fun getGeocachesByCategory(category: GeocacheType): LiveData<List<GeocacheWithHintsAndChallenges>> {
        return repository.getGeocachesByCategory(category)
    }



}