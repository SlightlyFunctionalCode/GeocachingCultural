package pt.ipp.estg.geocaching_cultural

import TestDaos
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.data.AppDatabase
import pt.ipp.estg.geocaching_cultural.data.classes.Challenge
import pt.ipp.estg.geocaching_cultural.data.classes.Enum.GeocacheType
import pt.ipp.estg.geocaching_cultural.data.classes.Geocache
import pt.ipp.estg.geocaching_cultural.data.classes.Hint
import pt.ipp.estg.geocaching_cultural.data.classes.Location
import pt.ipp.estg.geocaching_cultural.ui.components.navigation.NavigationDrawer
import pt.ipp.estg.geocaching_cultural.ui.components.navigation.StartNavBar
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import java.time.LocalDateTime
import java.util.Date

class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Geocaching_CulturalTheme {
                Surface() {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val navController = rememberNavController()
                    //NavigationDrawer(drawerState, navController)
                    //StartNavBar()
                    TestDaos()
                }
            }
        }
    }
}




