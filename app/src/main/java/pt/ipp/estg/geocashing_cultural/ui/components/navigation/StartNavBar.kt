package pt.ipp.estg.geocashing_cultural.ui.components.navigation

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
import pt.ipp.estg.geocashing_cultural.ui.screens.HomeScreen
import pt.ipp.estg.geocashing_cultural.ui.screens.LoginScreen
import pt.ipp.estg.geocashing_cultural.ui.screens.RegisterScreen
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme

@Composable
fun StartNavBar() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("homeScreen") }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            currentRoute = entry.destination.route?: "homeScreen"
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
                    ScaffoldContent(navController)
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
                    ScaffoldContent(navController)
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
            Button(
                contentPadding = PaddingValues(2.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                onClick = onLoginClick,
                content = { Text("Login") }
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    )
}

@Composable
fun ScaffoldContent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { HomeScreen(navController) }
        composable("loginScreen") { LoginScreen(navController) }
        composable("registerScreen") { RegisterScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleNavigationBarPreview() {
    Geocashing_CulturalTheme {
        StartNavBar()
    }
}



