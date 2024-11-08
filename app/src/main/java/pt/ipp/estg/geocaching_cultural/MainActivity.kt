package pt.ipp.estg.geocaching_cultural

import TestDaos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme

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




