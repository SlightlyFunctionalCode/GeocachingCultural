package pt.ipp.estg.geocaching_cultural.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.database.classes.UserWithGeocachesFound

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query(
        "SELECT * FROM User U " +
                "ORDER BY U.points DESC " +
                "LIMIT 10"
    )
    fun getTop10Users(): LiveData<List<User>>

    @Query(
        "SELECT * FROM User U " +
                "WHERE U.userId = :userId"
    )
    fun getUser(userId: Int): LiveData<User>

    @Query(
        "SELECT * FROM User U " +
                "WHERE U.userId = :userId"
    )
    fun getUserWithGeocachesCreated(userId: Int): LiveData<UserWithGeocachesCreated>

    @Query(
        "SELECT * FROM User U " +
                "WHERE U.userId = :userId"
    )
    fun getUserWithGeocachesFound(userId: Int): LiveData<UserWithGeocachesFound>

    @Query(
        "SELECT * FROM User U " +
                "WHERE U.email= :email AND U.password= :password"
    )
    suspend fun getUserWithLogin(email: String, password: String): User?
}
