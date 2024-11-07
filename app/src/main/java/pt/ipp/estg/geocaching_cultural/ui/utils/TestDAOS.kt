import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipp.estg.geocaching_cultural.data.AppDatabase
import pt.ipp.estg.geocaching_cultural.data.AppDatabaseTest
import pt.ipp.estg.geocaching_cultural.data.classes.Challenge
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType
import pt.ipp.estg.geocaching_cultural.data.classes.Geocache
import pt.ipp.estg.geocaching_cultural.data.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.data.classes.Hint
import pt.ipp.estg.geocaching_cultural.data.classes.Location
import java.time.LocalDateTime

@Composable
fun TestDaos() {
    val context = LocalContext.current
    var geocache by remember { mutableStateOf("Loading...") }
    var hints by remember { mutableStateOf("Loading...") }
    var challenges by remember { mutableStateOf("Loading...") }


    // Launching the coroutine in a Composable to perform the database operation off the main thread
    LaunchedEffect(Unit) {
        var geocacheWithHintsAndChallenges = testRelationships(context)
        geocache = geocacheWithHintsAndChallenges.geocache.toString()
        hints = geocacheWithHintsAndChallenges.hints.toString()
        challenges = geocacheWithHintsAndChallenges.challenges.toString()

    }

    Column {
        Text(text = geocache)
        Text(text = hints)
        Text(text = challenges)


    }
}

// Updated testRelationships function to be suspend
suspend fun testRelationships(context: Context): GeocacheWithHintsAndChallenges =
    withContext(Dispatchers.IO) {

        val geocacheDao = AppDatabaseTest.getDatabase(context).GeocacheDao()

        // Create test objects
        val location = Location(13.0, 32.0, "Rua da Rua")
        val testGeocache =
            Geocache(0, location, GeocacheType.CULTURAL, "Test Geocache", LocalDateTime.now(), 1)
        val testChallenge = Challenge(0, 1, "What is the answer?", "42", 32)
        val testHint = Hint(0, 1, "Hint for the challenge")

        // Insert data into the database
        geocacheDao.insertGeocache(testGeocache)
        geocacheDao.insertChallenge(testChallenge)
        geocacheDao.insertHint(testHint)

        // Fetch data with relationships
        val geocacheWithHintsAndChallenges = geocacheDao.getGeocacheWithHintsAndChallenges(1)

        // Verify the relationships are correct
        geocacheWithHintsAndChallenges

    }
