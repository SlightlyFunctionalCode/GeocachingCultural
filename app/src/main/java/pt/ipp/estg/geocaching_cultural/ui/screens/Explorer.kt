package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.services.LocationUpdateService
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import java.util.Locale

@Composable
fun ExplorerScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels? = null,
    usersViewsModels: UsersViewsModels? = null,
    geocacheType: GeocacheType? = null
) {
    var categorySelected by remember { mutableStateOf(geocacheType ?: GeocacheType.HISTORICO) }

    val user = usersViewsModels?.currentUser?.observeAsState()

    // Observa os geocaches da categoria selecionada
    var geocaches by remember { mutableStateOf(emptyList<GeocacheWithHintsAndChallenges>()) }

    LaunchedEffect(categorySelected) {
        geocacheViewsModels?.getGeocachesByCategory(categorySelected)
            ?.observeForever { geocachesList ->
                geocaches = geocachesList
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Explorar Geocaches", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(16.dp))

        CategoryLabel(
            categorySelected = categorySelected,
            onCategorySelectedChange = { newCategory ->
                categorySelected = newCategory
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Defina uma altura máxima para o LazyColumn
        ) {
            if (geocaches.isEmpty()) {
                item {
                    Text(
                        "Nenhuma geocache encontrada", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            } else {
                items(geocaches) { geocache ->
                    if (user != null) {
                        user.value?.let { MyGeocache(geocache, it.location) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryLabel(
    categorySelected: GeocacheType,
    onCategorySelectedChange: (GeocacheType) -> Unit
) {
    val categories = listOf(
        "GASTRONOMIA" to R.drawable.gastronomia,
        "CULTURAL" to R.drawable.cultural,
        "HISTORICO" to R.drawable.historico
    )

    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de categorias
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(8.dp)
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoryButton(
                name = categorySelected.name,
                icon = painterResource(id = categories.find { it.first == categorySelected.name }!!.second),
                isSelected = true,
                onClick = {}
            )

            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Mostrar categorias"
                )
            }
        }

        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                categories.forEach { (category, icon) ->
                    CategoryButton(
                        name = category,
                        icon = painterResource(id = icon),
                        isSelected = category == categorySelected.name,
                        onClick = {
                            onCategorySelectedChange(
                                GeocacheType.valueOf(
                                    category.uppercase(
                                        Locale.ROOT
                                    )
                                )
                            )
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryButton(name: String, icon: Painter, isSelected: Boolean, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(onClick = onClick)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = name,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(name)
    }
}

@Composable
fun MyGeocache(geocache: GeocacheWithHintsAndChallenges, userLocation: Location) {
    // Conteúdo principal
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = {})
    ) { // Adiciona um padding para o espaço do ícone
        Text(
            if (geocache.hints.isNotEmpty()) geocache.hints[0].hint else "Sem dicas",
            modifier = Modifier.padding(15.dp)
        )
        Text(
            text = "${
                String.format(
                    Locale.US,
                    "%.1f",
                    LocationUpdateService.getDistanceToGeocache(
                        userLocation,
                        geocache.geocache.location
                    ) / 1000
                )
            } Km",
            modifier = Modifier
                .align(Alignment.End)
                .padding(3.dp)
        )
    }
}


@Preview
@Composable
fun ExplorarPreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                ExplorerScreen(navController)
            }
        }
    }
}