package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow

@Composable
fun ExplorarScreen(navController: NavHostController, geocacheViewsModels: GeocacheViewsModels) {
    var categoriaSelecionada by remember { mutableStateOf(GeocacheType.HISTORICO) }

    // Observa os geocaches da categoria selecionada
    val geocaches by geocacheViewsModels.getGeocachesByCategory(categoriaSelecionada).observeAsState(emptyList())
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Explorar Geocaches", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(16.dp))

        CategoriaScreen(
            categoriaSelecionada = categoriaSelecionada,
            onCategoriaSelecionadaChange = { novaCategoria ->
                categoriaSelecionada = novaCategoria
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe a lista de geocaches da categoria selecionada
        LazyColumn {
            items(geocaches) { geocache ->
                MyGeocache(geocache)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CategoriaScreen(
    categoriaSelecionada: GeocacheType,
    onCategoriaSelecionadaChange: (GeocacheType) -> Unit
) {
    val categorias = listOf(
        "Gastronomia" to R.drawable.gastronomia,
        "Cultura" to R.drawable.cultural,
        "Histórico" to R.drawable.historico
    )

    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de categorias
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CategoriaButton(
                nome = categoriaSelecionada.name,
                icon = painterResource(id = categorias.find { it.first == categoriaSelecionada.name }!!.second),
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
                categorias.forEach { (categoria, icon) ->
                    CategoriaButton(
                        nome = categoria,
                        icon = painterResource(id = icon),
                        isSelected = categoria == categoriaSelecionada.name,
                        onClick = {
                            onCategoriaSelecionadaChange(categoriaSelecionada)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriaButton(nome: String, icon: Painter, isSelected: Boolean, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .background(Color.Transparent, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = nome,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(nome)
    }
}

@Composable
fun MyGeocache(geocache: GeocacheWithHintsAndChallenges) {
    // Conteúdo principal
    Column(
        modifier = Modifier
            .clickable(onClick = {})
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
    ) { // Adiciona um padding para o espaço do ícone
        Text(geocache.hints[0].hint, modifier = Modifier.padding(15.dp))
        Text(geocache.geocache.name, modifier = Modifier
            .align(Alignment.End)
            .padding(3.dp))
    }
}


@Preview
@Composable
fun ExplorarPreview() {
    val navController = rememberNavController()
    val geocacheViewsModels: GeocacheViewsModels = viewModel()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                ExplorarScreen(navController, geocacheViewsModels)
            }
        }
    }
}