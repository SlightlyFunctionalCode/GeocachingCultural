package pt.ipp.estg.geocaching_cultural.data

import android.content.Context
import androidx.room.*
import pt.ipp.estg.geocaching_cultural.data.classes.Challenge
import pt.ipp.estg.geocaching_cultural.data.classes.Geocache
import pt.ipp.estg.geocaching_cultural.data.classes.Hint
import pt.ipp.estg.geocaching_cultural.data.classes.Notification
import pt.ipp.estg.geocaching_cultural.data.classes.User
import pt.ipp.estg.geocaching_cultural.data.classes.UserGeocacheFoundCrossRef
import pt.ipp.estg.geocaching_cultural.data.classes.converters.LocalDateTimeConverter
import pt.ipp.estg.geocaching_cultural.data.dao.GeocacheDao
import pt.ipp.estg.geocaching_cultural.data.dao.UserDao

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