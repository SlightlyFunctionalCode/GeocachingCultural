package pt.ipp.estg.geocaching_cultural

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.components.navigation.NavigationDrawer
import pt.ipp.estg.geocaching_cultural.ui.components.navigation.StartNavBar
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme

class MainActivity : ComponentActivity() {
    // Define the permission launcher
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Check the results for both FINE and COARSE location permissions
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                // Precise location access granted
                Log.d("Permissions", "ACCESS_FINE_LOCATION granted")
                // Start location-related tasks here
            }

            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                // Only approximate location access granted
                Log.d("Permissions", "ACCESS_COARSE_LOCATION granted")
                // Start tasks that only require approximate location
            }

            else -> {
                // No location access granted
                Log.d("Permissions", "No location permissions granted")
                // Handle lack of permissions gracefully
            }
        }
    }

    private fun requestLocationPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Geocaching_CulturalTheme {
                Surface() {
                    val usersViewsModels: UsersViewsModels = viewModel()
                    val geocachesViewsModels: GeocacheViewsModels = viewModel()

                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val navController = rememberNavController()

                    var currentRoute by remember { mutableStateOf("aboutUsScreen") }

                    val registerScreenPattern = Regex("registerScreen/.+")

                    LaunchedEffect(navController) {
                        navController.currentBackStackEntryFlow.collect { entry ->
                            currentRoute = entry.destination.route ?: "aboutUsScreen"
                        }
                    }

                    if (currentRoute == "aboutUsScreen" || currentRoute == "loginScreen" || registerScreenPattern.matches(
                            currentRoute
                        )
                    ) {
                        StartNavBar(navController, usersViewsModels, geocachesViewsModels)
                    } else {
                        NavigationDrawer(
                            drawerState = drawerState,
                            navController = navController,
                            usersViewsModels = usersViewsModels,
                            geocacheViewsModels = geocachesViewsModels
                        )
                    }

                    requestLocationPermissions()
                }
            }
        }
    }
}

@Composable
fun DisplayUser(user: User) {
    Column {
        Text(text = "Id: ${user.userId}")
        Text(text = "Nome: ${user.name}")
        Text(text = "Location: ${user.location.lat},${user.location.lng})")
        Text(text = "Points: ${user.points}")
        Text(text = "Email: ${user.email}")
        Text(text = "Password: ${user.password}")
    }
}






