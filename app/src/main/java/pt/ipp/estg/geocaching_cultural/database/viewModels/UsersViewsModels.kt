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
import pt.ipp.estg.geocaching_cultural.database.AppDatabase
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesFound
import pt.ipp.estg.geocaching_cultural.database.repositories.UserRepository

class UsersViewsModels(application: Application) : AndroidViewModel(application) {

    private val _currentUserId = MutableLiveData<Int>()
    private val _currentUser = MediatorLiveData<User>()

    val currentUserId: LiveData<Int> get() = _currentUserId
    val currentUser: LiveData<User> get() = _currentUser

    val repository: UserRepository
    //val allBooks : LiveData<List<Book>>

    init {
        val db = AppDatabase.getDatabase(application)
        //val restAPI = RetrofitHelper.getInstance().create(BookAPI::class.java)
        repository = UserRepository(db.UserDao()/*restAPI*/)
        //allBooks = repository.getBooks()
        //this.updateBooksOnline()

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
                        user ?: User(userId, "Unknown", "", "", 0, null, Location(0.0, 0.0, ""))
                }
            }
        }
    }

    fun getTop10Users(): LiveData<List<User>> {
        viewModelScope.launch(Dispatchers.IO) {
            // updateBooksOnline();
        }
        return repository.getTop10Users()
    }

    fun getUsersWithGeocachesCreated(id: Int): LiveData<UserWithGeocachesCreated> {
        return repository.getUserWithGeocachesCreated(id)
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

    fun getUser(id: Int): LiveData<User> {
        return repository.getUser(id)
    }

    fun getUserWithGeocachesCreated(id: Int): LiveData<UserWithGeocachesCreated> {
        return repository.getUserWithGeocachesCreated(id)
    }

    fun getUserWithGeocachesFound(id: Int): LiveData<UserWithGeocachesFound> {
        return repository.getUserWithGeocachesFound(id)
    }

    // Save and update the current user ID in both LiveData and SharedPreferences
    fun saveCurrentUserId(userId: Int) {
        _currentUserId.postValue(userId)  // Use postValue for thread-safety
        saveCurrentUserIdToPreferences(userId) // Save to SharedPreferences

        viewModelScope.launch(Dispatchers.IO) {
            _currentUser.postValue(repository.getUser(userId).value)
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

    /* TODO: adicionar rest api*/
    /*fun updateBooksOnline(){
        viewModelScope.launch(Dispatchers.IO){
            val response = repository.updateBooksOnline()
            if (response.isSuccessful){
                val content = response.body()
                content?.docs?.forEach {
                    insertBook(User(it.key,it.title,it.author_name.toString(),it.year))
                }
            }
        }
    }*/

}
