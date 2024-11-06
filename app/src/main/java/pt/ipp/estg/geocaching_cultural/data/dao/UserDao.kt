package pt.ipp.estg.geocaching_cultural.data.dao

import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.User
import pt.ipp.estg.geocaching_cultural.data.classes.UserWithGeocachesCreated
import pt.ipp.estg.geocaching_cultural.data.classes.UserWithGeocachesFound

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM User U " +
            "ORDER BY U.points DESC " +
            "LIMIT 10")
    fun getTop10Users():List<User>

    @Query("SELECT * FROM User U " +
            "WHERE U.userId = :userId")
    fun getUser(userId: Int): User

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserWithGeocachesCreated(userId: Int): UserWithGeocachesCreated

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserWithGeocachesFound(userId: Int): UserWithGeocachesFound
}
