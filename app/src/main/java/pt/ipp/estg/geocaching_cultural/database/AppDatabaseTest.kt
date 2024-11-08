package pt.ipp.estg.geocaching_cultural.database

import android.content.Context
import androidx.room.*
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Notification
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.database.classes.converters.LocalDateTimeConverter
import pt.ipp.estg.geocaching_cultural.database.dao.GeocacheDao
import pt.ipp.estg.geocaching_cultural.database.dao.UserDao

@Database(
    entities = [
        User::class,
        Geocache::class,
        Challenge::class,
        Hint::class,
        Notification::class,
        UserGeocacheFoundCrossRef::class],
    version = 2
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabaseTest : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun GeocacheDao(): GeocacheDao

    companion object {
        private var INSTANCE: AppDatabaseTest? = null

        fun getDatabase(context: Context): AppDatabaseTest {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.inMemoryDatabaseBuilder(
                    context.applicationContext,
                    AppDatabaseTest::class.java,
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}