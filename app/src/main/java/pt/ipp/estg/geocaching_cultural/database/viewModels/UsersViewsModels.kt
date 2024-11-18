package pt.ipp.estg.geocaching_cultural.database.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesFound
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository
import pt.ipp.estg.geocaching_cultural.services.LocationUpdateService
import pt.ipp.estg.geocaching_cultural.services.SensorService

class UsersViewsModels(application: Application) : AndroidViewModel(application) {
    private val _currentUserId = MutableLiveData<Int>()
    private val _currentUser = MediatorLiveData<User>()
     val locationUpdateService: LocationUpdateService
    private val sensorService: SensorService
    var isUpdatingLocation = false

    val currentUserId: LiveData<Int> get() = _currentUserId
    val currentUser: LiveData<User> get() = _currentUser

    private val repository: UserRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UserRepository(db.UserDao())

        locationUpdateService = LocationUpdateService(application, this)
        sensorService = SensorService(application, this)

        // Load the current user ID from SharedPreferences
        _currentUserId.value = loadCurrentUserIdFromPreferences()

        // Observe currentUserId changes to update currentUser
        _currentUserId.observeForever { userId ->
            if (userId != -1) {
                // Remove previous source if there's an active one
                _currentUser.value?.let { _currentUser.removeSource(repository.getUser(userId)) }

                val userLiveData = repository.getUser(userId)

                // Add source to _currentUser and observe the user data
                _currentUser.addSource(userLiveData) { user ->
                    _currentUser.value =
                        user ?: User(
                            userId,
                            "Unknown",
                            "",
                            "",
                            0,
                            null,
                            R.drawable.avatar_male_01,
                            false,
                            Location(0.0, 0.0)
                        )
                }
            }
        }
    }

    fun startLocationUpdates(context: Context) {
        if (!isUpdatingLocation) {
            locationUpdateService.startLocationUpdates(context)
            isUpdatingLocation = true
        }
    }

    fun stopLocationUpdates() {
        if (isUpdatingLocation) {
            locationUpdateService.stopLocationUpdates()
            isUpdatingLocation = false
        }
    }

    fun startSensorUpdates(context: Context) {
        // Start location updates when the user is available
        sensorService.startSensorUpdates(context = context)
    }

    fun stopSensorUpdates() {
        // Stop location updates when no longer needed
        sensorService.stopSensorUpdates()
    }

    fun getTop10Users(): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO) {
            // updateBooksOnline();
        }
        return repository.getTop10Users()
    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(user)
        }
    }

    fun insertUserGeocacheFound(userGeocacheFound: UserGeocacheFoundCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUserGeocacheFound(userGeocacheFound)
        }
    }

    fun getUserWithLogin(email: String, password: String, callback: (User?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUserWithLogin(email, password)
            callback(user)
        }
    }

    fun getUserWithGeocachesCreated(id: Int): LiveData<UserWithGeocachesCreated> {
        return repository.getUserWithGeocachesCreated(id)
    }

    fun getUserWithGeocachesFound(id: Int): LiveData<UserWithGeocachesFound> {
        return repository.getUserWithGeocachesFound(id)
    }

    // Save and update the current user ID in both LiveData and SharedPreferences
    fun saveCurrentUserId(userId: Int, onComplete: (() -> Unit)? = null) {
        _currentUserId.postValue(userId)  // Use postValue for thread-safety
        saveCurrentUserIdToPreferences(userId) // Save to SharedPreferences

        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(repository.getUser(userId).value)
            // Invoke the callback after completion
            withContext(Dispatchers.Main) {
                onComplete?.invoke()
            }
        }
    }


    // Helper functions for SharedPreferences
    private fun saveCurrentUserIdToPreferences(userId: Int) {
        val sharedPreferences = getApplication<Application>()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("current_user_id", userId).apply()
    }

    private fun loadCurrentUserIdFromPreferences(): Int {
        val sharedPreferences = getApplication<Application>()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("current_user_id", -1)
    }
}
