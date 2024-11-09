package pt.ipp.estg.geocaching_cultural.ui.components.navigation

import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavHostController
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton

@Composable
fun StartNavBar(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    var currentRoute by remember { mutableStateOf("aboutUsScreen") }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            currentRoute = entry.destination.route ?: "aboutUsScreen"
        }
    }

    if (currentRoute == "aboutUsScreen") {
        Scaffold(
            topBar = {
                StartTopAppBar(onLoginClick = {
                    navController.navigate("loginScreen")
                })
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    MyScaffoldContent(navController, usersViewsModels, geocacheViewsModels)
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
                    MyScaffoldContent(navController, usersViewsModels, geocacheViewsModels)
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



