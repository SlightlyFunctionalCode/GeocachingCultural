package pt.ipp.estg.geocashing_cultural.ui.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.screens.ExplorarScreen
import pt.ipp.estg.geocashing_cultural.ui.screens.PrincipalScreen
import pt.ipp.estg.geocashing_cultural.ui.screens.ProfileEditingScreen
import pt.ipp.estg.geocashing_cultural.ui.utils.MyIconButton
import pt.ipp.estg.geocashing_cultural.ui.screens.ProfileScreen
import pt.ipp.estg.geocashing_cultural.ui.theme.DarkGray
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.White

@Composable
fun NavigationDrawer(drawerState: DrawerState, navController: NavHostController) {
    val drawerItemList = prepareNavigationDrawerItems()
    var selectedItem by remember { mutableStateOf(drawerItemList[0]) }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .wrapContentWidth(align = Alignment.Start)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)

                    ) {
                        Text(text = "Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        drawerItemList.forEach { item ->
                            DrawerItem(
                                item = item,
                                selectedItem = selectedItem,
                                updateSelected = { selectedItem = it },
                                navController = navController,
                                drawerState = drawerState
                            )
                        }
                    }
                }
            }
        }, gesturesEnabled = true, content = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Scaffold(drawerState = drawerState, navController = navController)
            }
        })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItem(
    item: NavigationDrawerData,
    selectedItem: NavigationDrawerData,
    updateSelected: (i: NavigationDrawerData) -> Unit,
    navController: NavHostController,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = White
            )
        },
        label = { Text(text = item.label, color = White) },
        selected = (item == selectedItem),
        onClick = {
            coroutineScope.launch {
                navController.navigate(item.route)
                drawerState.close()
            }
            updateSelected(item)
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(drawerState: DrawerState, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {
        TopAppBar {
            coroutineScope.launch {
                drawerState.open()
            }
        }
    }, content = { padding ->
        Column(modifier = Modifier.padding(padding)) {
            MyScaffoldContent(navController)
        }
    }, bottomBar = {
        Footer()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(onNavIconClick: () -> Unit) {
    TopAppBar(title = {
        Text(
            text = "GeoCultura Explorer", fontWeight = FontWeight.Bold, fontSize = 6.em
        )
    }, actions = {
        Row(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            IconButton({}) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "search",
                    tint = DarkGray,
                    modifier = Modifier.size(45.dp)
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = White, shape = CircleShape
                    )
                    .size(45.dp)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = stringResource(id = R.string.avatar_description)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            MyIconButton(Icons.Default.Menu, { onNavIconClick() }, modifier = Modifier)
        }
    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    )
    )
}

@Composable
fun MyScaffoldContent(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "principalScreen") {
        composable("principalScreen") { PrincipalScreen(navController) }
        composable("explorarScreen") { ExplorarScreen(navController) }
        composable("profileScreen") { ProfileScreen(navController) }
        composable("profileEditingScreen") { ProfileEditingScreen(navController) }
    }
}

private fun prepareNavigationDrawerItems(): List<NavigationDrawerData> {
    val drawerItemsList = arrayListOf<NavigationDrawerData>()
    drawerItemsList.add(NavigationDrawerData(label = "Principal", route = "principalScreen", icon = Icons.Filled.Home))
    drawerItemsList.add(NavigationDrawerData(label = "Explorar",route = "explorarScreen", icon = Icons.Filled.LocationOn))
    drawerItemsList.add(NavigationDrawerData(label = "Perfil", route = "profileScreen", icon = Icons.Filled.Person))

    return drawerItemsList
}

data class NavigationDrawerData(val label: String, val route: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
    Geocashing_CulturalTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val navController = rememberNavController()
        NavigationDrawer(drawerState, navController)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Geocashing_CulturalTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navController = rememberNavController()
        NavigationDrawer(drawerState, navController)
    }
}