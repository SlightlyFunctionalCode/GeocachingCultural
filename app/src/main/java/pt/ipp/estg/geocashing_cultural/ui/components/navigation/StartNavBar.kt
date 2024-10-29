package pt.ipp.estg.geocashing_cultural.ui.components.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartNavBar() {
    val navController = rememberNavController()
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartTopAppBar(onLoginClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "GeoCultura Explorer", fontWeight = FontWeight.Bold) },
        actions = {
            Button(
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
    NavHost(navController = navController, startDestination = "startScreen") {
        composable("startScreen") { StartScreen() }
        composable("loginScreen") { LoginScreen() } // Aqui define a nova LoginScreen
    }
}
@Composable
fun StartScreen() {
    // Implementação básica da tela de início
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Start Screen")
    }
}


@Composable
fun LoginScreen() {
    // Implementação básica da tela de login
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Login Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleNavigationBarPreview() {
    Geocashing_CulturalTheme {
        StartNavBar()
    }
}



