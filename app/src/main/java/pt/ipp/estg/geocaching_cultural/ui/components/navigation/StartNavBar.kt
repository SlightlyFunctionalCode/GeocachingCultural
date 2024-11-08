package pt.ipp.estg.geocaching_cultural.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.screens.HomeScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.LoginScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.RegisterScreen
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton

@Composable
fun StartNavBar(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    var currentRoute by remember { mutableStateOf("homeScreen") }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            currentRoute = entry.destination.route ?: "homeScreen"
        }
    }

    if (currentRoute == "homeScreen") {
        Scaffold(
            topBar = {
                StartTopAppBar(onLoginClick = {
                    navController.navigate("loginScreen")
                })
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    MyScaffoldContent(navController, usersViewsModels)
                }
            },
            bottomBar = {
                Footer()
            }
        )
    } else {
        Scaffold(
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    MyScaffoldContent(navController, usersViewsModels)
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartTopAppBar(onLoginClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "GeoCultura Explorer", fontWeight = FontWeight.Bold) },
        actions = {
            MyTextButton(text = "Login", onClick = onLoginClick)
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SimpleNavigationBarPreview() {
    Geocaching_CulturalTheme {
       // StartNavBar(rememberNavController())
    }
}



