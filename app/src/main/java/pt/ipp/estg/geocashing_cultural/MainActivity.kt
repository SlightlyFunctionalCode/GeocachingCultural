package pt.ipp.estg.geocashing_cultural

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.tooling.preview.Preview
import pt.ipp.estg.geocashing_cultural.ui.components.navigation.NavigationDrawer
import pt.ipp.estg.geocashing_cultural.ui.components.navigation.StartNavBar
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Geocashing_CulturalTheme {
                Surface(
                    color = Yellow
                ) {
                    StartNavBar() // Chama o composable principal
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Geocashing_CulturalTheme {
        Greeting("Android")
    }
}