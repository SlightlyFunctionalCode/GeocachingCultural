package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow
import pt.ipp.estg.geocashing_cultural.ui.utils.LargeVerticalSpacer
import pt.ipp.estg.geocashing_cultural.ui.utils.MyGeocachePertoDeMim
import pt.ipp.estg.geocashing_cultural.ui.utils.SmallVerticalSpacer

@Composable
fun ExplorarScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Explorar Geocaches", fontSize = 22.sp)

        Spacer(modifier = Modifier.height(16.dp))

        CategoriaScreen()
    }
}

@Composable
fun CategoriaScreen() {
    // Lista de categorias e ícones
    val categorias = listOf(
        "Gastronomia" to R.drawable.gastronomia,
        "Cultura" to R.drawable.cultural,
        "Histórico" to R.drawable.historico,
    )

    // Categoria selecionada e estado de expansão
    var categoriaSelecionada by remember { mutableStateOf(categorias[0].first) }
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
            // Exibe apenas a categoria selecionada
            CategoriaButton(
                nome = categoriaSelecionada,
                icon = painterResource(id = categorias.find { it.first == categoriaSelecionada }!!.second),
                isSelected = true,
                onClick = { /* Não faz nada, já está selecionada */ }
            )

            // Botão para expandir/contrair a lista de categorias
            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                    contentDescription = if (isExpanded) "Fechar categorias" else "Mostrar categorias"
                )
            }
        }

        // Lista de categorias (exibida apenas se isExpanded for true)
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
                        isSelected = categoria == categoriaSelecionada,
                        onClick = {
                            categoriaSelecionada = categoria
                            isExpanded = false // Fecha a lista após a seleção
                        }
                    )
                }
            }
        }

        // Conteúdo que muda com a categoria selecionada
        Spacer(modifier = Modifier.height(16.dp))
        when (categoriaSelecionada) {
            "Gastronomia" -> GastronomiaContent()
            "Cultura" -> CulturaContent()
            "Histórico" -> HistoricoContent()
        }
    }
}

@Composable
fun GastronomiaContent() {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        MyGeocache(
            "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas.\"",
            "5.0Km"
        )
        SmallVerticalSpacer()
        // Alterar este número conforme necessário
        MyGeocache(
            "\"Café muito frequentemente visitado por estudantes.\"",
            "5.7Km"
        )

        SmallVerticalSpacer()
    }
}

@Composable
fun CulturaContent() {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
// Alterar este número conforme necessário
        MyGeocache(
            "\"Edificício com heróis que lutampelo nosso país.\"",
            "5.6Km"
        )

        SmallVerticalSpacer()
    }
}

@Composable
fun HistoricoContent() {
    Column(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Alterar este número conforme necessário
        MyGeocache(
            "\"Sua fachada imponente e suas pedras centenárias contam histórias de fé e tradição.\"",
            "5.5Km"
        )

        SmallVerticalSpacer()
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
fun MyGeocache(titulo: String, distancia: String) {
    // Conteúdo principal
    Column(
        modifier = Modifier
            .clickable(onClick = {})
            .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
    ) { // Adiciona um padding para o espaço do ícone
        Text(titulo, modifier = Modifier.padding(15.dp))
        Text(distancia, modifier = Modifier
            .align(Alignment.End)
            .padding(3.dp))
    }
}

@Preview
@Composable
fun ExplorarPreview() {
    val navController = rememberNavController()

    Geocashing_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow) // Set the background color here
        ) {
            item {
                ExplorarScreen(navController)
            }
        }
    }
}