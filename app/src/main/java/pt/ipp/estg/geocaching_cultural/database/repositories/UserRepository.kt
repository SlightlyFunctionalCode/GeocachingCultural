package pt.ipp.estg.geocaching_cultural.database.repositories

import androidx.lifecycle.LiveData
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesFound
import pt.ipp.estg.geocaching_cultural.database.dao.UserDao

class UserRepository(val userDao: UserDao /*val restAPI: BookAPI*/) {

    fun getTop10Users(): LiveData<List<User>> {
        return userDao.getTop10Users()
    }

    /*
    suspend fun updateBooksOnline(): Response<BookDataResponse> {
        return this.restAPI.getBooks("android")
    }
    */

    fun getUser(id: Int): LiveData<User> {
        return userDao.getUser(id)
    }

    fun getUserWithGeocachesCreated(id: Int): LiveData<UserWithGeocachesCreated> {
        return userDao.getUserWithGeocachesCreated(id)
    }

    fun getUserWithGeocachesFound(id: Int): LiveData<UserWithGeocachesFound> {
        return userDao.getUserWithGeocachesFound(id)
    }

    suspend fun getUserWithLogin(email: String, password: String): User? {
        return userDao.getUserWithLogin(email, password)
    }

    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun update(user: User) {
        userDao.updateUser(user)
    }

    suspend fun delete(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun insertUserGeocacheFound(userGeocacheFound: UserGeocacheFoundCrossRef) {
        userDao.insertUserGeocacheFound(userGeocacheFound)
    }
}
