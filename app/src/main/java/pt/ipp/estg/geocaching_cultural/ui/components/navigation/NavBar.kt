package pt.ipp.estg.geocaching_cultural.ui.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.screens.ActiveGeocacheScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.CreateGeocacheScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.CreatedGeocacheDetailsScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.CreatedGeocachesScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.ExplorarScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.GeocacheFoundScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.GeocacheNotFoundScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.HistoryScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.AboutUsScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.LoginScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.HomeScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.ProfileEditingScreen
import pt.ipp.estg.geocaching_cultural.ui.utils.MyIconButton
import pt.ipp.estg.geocaching_cultural.ui.screens.ProfileScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.RegisterScreen
import pt.ipp.estg.geocaching_cultural.ui.screens.ScoreboardScreen
import pt.ipp.estg.geocaching_cultural.ui.theme.Blue
import pt.ipp.estg.geocaching_cultural.ui.theme.DarkBlue
import pt.ipp.estg.geocaching_cultural.ui.theme.DarkGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.White
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    val drawerItemList = prepareNavigationDrawerItems()
    var selectedItem by remember { mutableStateOf(drawerItemList[0]) }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ModalDrawerSheet(
                    drawerContainerColor = DarkBlue,
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
                Scaffold(
                    drawerState = drawerState,
                    navController = navController,
                    usersViewsModels,
                    geocacheViewsModels
                )
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
fun Scaffold(
    drawerState: DrawerState,
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(containerColor = Yellow,
        topBar = {
            MyTopAppBar(
                onNavIconClick = {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }, usersViewsModels = usersViewsModels, geocacheViewsModels = geocacheViewsModels
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxHeight(1f)
            ) {
                item { MyScaffoldContent(navController, usersViewsModels, geocacheViewsModels) }
            }
        },
        bottomBar = {
            Footer()
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    onNavIconClick: () -> Unit,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
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
                var userState = usersViewsModels.currentUser.observeAsState()

                userState.value?.let { user ->

                    if (user.profileImageUrl == null) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = stringResource(id = R.string.avatar_description)
                        )
                    } else {
                        GlideImage(
                            imageModel = { user.profileImageUrl }, // loading a network image using an URL.
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop, alignment = Alignment.Center
                            ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            MyIconButton(Icons.Default.Menu, { onNavIconClick() }, modifier = Modifier)
        }
    }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = Blue
    )
    )
}

@Composable
fun MyScaffoldContent(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    NavHost(navController = navController, startDestination = "aboutUsScreen") {
        composable("aboutUsScreen") { AboutUsScreen(navController) }
        composable("loginScreen") { LoginScreen(navController, usersViewsModels) }
        composable("registerScreen/{parameter}") { backStackEntry ->
            val parameter = backStackEntry.arguments?.getString("parameter")
            RegisterScreen(navController, usersViewsModels, parameter)
        }
        composable("homeScreen") { HomeScreen(navController, geocacheViewsModels) }
        composable("explorarScreen") { ExplorarScreen(navController, geocacheViewsModels) }
        composable("createGeocacheScreen") {
            CreateGeocacheScreen(
                navController,
                geocacheViewsModels,
                usersViewsModels
            )
        }
        composable("scoreboardScreen") { ScoreboardScreen(navController, usersViewsModels) }
        composable("profileScreen") { ProfileScreen(navController, usersViewsModels) }
        composable("profileEditingScreen") { ProfileEditingScreen(navController) }
        composable("historyScreen") { HistoryScreen(navController) }
        composable("activeGeocacheScreen") { ActiveGeocacheScreen(navController) }
        composable("geocacheNotFoundScreen") { GeocacheNotFoundScreen(navController) }
        composable("createdGeocachesScreen") { CreatedGeocachesScreen(navController) }
        composable("createdGeocacheDetailsScreen") { CreatedGeocacheDetailsScreen(navController) }
        composable("geocacheFoundScreen") { GeocacheFoundScreen(navController) }
    }
}

private fun prepareNavigationDrawerItems(): List<NavigationDrawerData> {
    val drawerItemsList = arrayListOf<NavigationDrawerData>()
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Home",
            route = "homeScreen",
            icon = Icons.Filled.Home
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Explorar",
            route = "explorarScreen",
            icon = Icons.Filled.LocationOn
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "CreateGeocache",
            route = "createGeocacheScreen",
            icon = Icons.Filled.Add
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Scoreboard",
            route = "scoreboardScreen",
            icon = Icons.Filled.Star
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Perfil",
            route = "profileScreen",
            icon = Icons.Filled.Person
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Histórico",
            route = "historyScreen",
            icon = Icons.AutoMirrored.Filled.List
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Geocache Atual",
            route = "activeGeocacheScreen",
            icon = Icons.Filled.PlayArrow
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Geocaches Criados",
            route = "createdGeocachesScreen",
            icon = Icons.Filled.PlayArrow
        )
    )
    drawerItemsList.add(
        NavigationDrawerData(
            label = "Geocache Found",
            route = "geocacheFoundScreen",
            icon = Icons.Filled.PlayArrow
        )
    )

    return drawerItemsList
}

data class NavigationDrawerData(val label: String, val route: String, val icon: ImageVector)

@Preview(showBackground = true)
@Composable
fun NavigationDrawerPreview() {
    Geocaching_CulturalTheme {
        val usersViewsModels: UsersViewsModels = viewModel()
        val geocacheViewsModels: GeocacheViewsModels = viewModel()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val navController = rememberNavController()
        NavigationDrawer(drawerState, navController, usersViewsModels, geocacheViewsModels)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Geocaching_CulturalTheme {
        val usersViewsModels: UsersViewsModels = viewModel()
        val geocacheViewsModels: GeocacheViewsModels = viewModel()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val navController = rememberNavController()
        NavigationDrawer(drawerState, navController, usersViewsModels, geocacheViewsModels)
    }
}