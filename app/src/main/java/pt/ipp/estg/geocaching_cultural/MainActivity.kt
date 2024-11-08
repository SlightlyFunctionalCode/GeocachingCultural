package pt.ipp.estg.geocaching_cultural

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.components.navigation.NavigationDrawer
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField

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
                    RoomDemoWithViewModel()
                }
            }
        }
    }


}

@Composable
fun RoomDemoWithViewModel() {
    val usersViewsModels: UsersViewsModels = viewModel()

    val users = usersViewsModels.getTop10Users().observeAsState()

    val user = usersViewsModels.getUser(5).observeAsState()

    val currentUser = usersViewsModels.currentUser.observeAsState()
    val currentUserId = usersViewsModels.currentUserId.observeAsState()


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        users.value?.forEach {
            DisplayUser(user = it)
        }

        Button(onClick = {
            val location = Location(13.0, 32.0, "Rua da Rua")

            usersViewsModels.insertUser(
                User(
                    0,
                    "Missing",
                    "test@mail.com",
                    "12345678",
                    0,
                    "",
                    location
                )
            )
        }
        ) {
            Text(text = "Add New User")
        }


        var userIdString by remember { mutableStateOf("") }

        MyTextField(userIdString, onValueChange = { userIdString = it })

        Button(onClick = {
            try {
                val userIdInt = userIdString.toInt()
                usersViewsModels.saveCurrentUserId(userIdInt)
            } catch (e: NumberFormatException) {
                println("Error converting to Int: ${e.message}")
                // Consider showing an error message to the user here
            }
        }) {
            Text(text = "Set current UserId")
        }

        Button(onClick = {
            try {
                val userIdInt = userIdString.toInt()
                val userToBeDeleted = usersViewsModels.getUser(userIdInt).value
                if (userToBeDeleted != null) {
                    usersViewsModels.deleteUser(userToBeDeleted)
                }
            } catch (e: NumberFormatException) {
                println("Error converting to Int: ${e.message}")
                // Consider showing an error message to the user here
            }
        }) {
            Text(text = "Delete user")
        }


        Text("Get User 5")
        user.value?.let { DisplayUser(it) }

        Text("Get CurrentUser: -->"+currentUserId.value)

        currentUser.value?.let { DisplayUser(it) }
    }
}

@Composable
fun DisplayUser(user: User) {
    Column {
        Text(text = "Id: ${user.userId}")
        Text(text = "Nome: ${user.name}")
        Text(text = "Location: ${user.location.address},(${user.location.latitude},${user.location.longitude})")
        Text(text = "Points: ${user.points}")
        Text(text = "Email: ${user.email}")
        Text(text = "Password: ${user.password}")
    }
}





