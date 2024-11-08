package pt.ipp.estg.geocaching_cultural.database.repositories

import androidx.lifecycle.LiveData
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.dao.UserDao

class UserRepository(val userDao: UserDao, /*val restAPI: BookAPI*/) {

    fun getTop10Users(): LiveData<List<User>> {
        return userDao.getTop10Users()
    }

    /*
    suspend fun updateBooksOnline(): Response<BookDataResponse> {
        return this.restAPI.getBooks("android")
    }
    */

    fun getUser(id:Int): LiveData<(User)> {
        return userDao.getUser(id)
    }

    suspend fun insert(user:User){
        userDao.insertUser(user)
    }

    suspend fun update(user:User){
        userDao.updateUser(user)
    }

    suspend fun delete(user:User){
        userDao.deleteUser(user)
    }
}
