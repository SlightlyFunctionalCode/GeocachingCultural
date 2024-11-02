package pt.ipp.estg.geocashing_cultural

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.rememberNavController
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
                Surface() {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val navController = rememberNavController()
                    NavigationDrawer(drawerState,navController)
                    //StartNavBar()


                }
            }
        }
    }
}



