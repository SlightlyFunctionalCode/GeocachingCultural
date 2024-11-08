package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import java.time.LocalDateTime

@Composable
fun CreateGeocacheScreen(navController: NavHostController, viewModel: GeocacheViewsModels = viewModel()) {
    val geocacheViewsModels: GeocacheViewsModels = viewModel()
    // Lista de dicas inicializada com três caixas de dica
    val dicas = remember { mutableStateListOf("Dica 1", "Dica 2", "Dica 3") }
    val perguntas = remember {mutableStateListOf("Pergunta para 5km", "Pergunta para 1km", "Pergunta para 500m", "Pergunta Final")}
    // Estado para a categoria selecionada e localização
    val categorias = listOf("Histórico", "Cultural", "Gastronômico") // Lista de categorias
    var categoriaSelecionada = GeocacheType.HISTORICO
    var localizacao by remember { mutableStateOf(Location(0.0, 0.0, "")) }
    val hints by remember { mutableStateOf(mutableListOf<Hint>()) }
    val challenges by remember { mutableStateOf(mutableListOf<Challenge>()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        dicas.forEachIndexed { index, dica ->
            LabelDica(
                label = dica, isRemovable = index >= 3,
                onRemoveClick = { dicas.removeAt(index); hints.removeAt(index) }
            )
            Spacer(Modifier.padding(5.dp))
        }
            // Botão para adicionar uma nova dica
        Button(onClick = { dicas.add("Dica ${dicas.size + 1}"); hints.add(Hint(0, 0, "")) }) {
            Text(text = "Adicionar Dica")
        }

        perguntas.forEachIndexed { index, pergunta ->
            LabelPergunta(label = pergunta, onLabelChange = { challenges[index].question = it },
                onAnswerChange = { challenges[index].correctAnswer = it })
            Spacer(Modifier.padding(5.dp))
        }


            // Caixa de seleção para categoria
            CategoriaDropdown(
                categorias = categorias,
                categoriaSelecionada = categoriaSelecionada.toString(),
                onCategoriaSelecionada = {
                    if(it == categorias[0])
                        categoriaSelecionada = GeocacheType.HISTORICO
                    else if( it == categorias[1]) {
                        categoriaSelecionada = GeocacheType.CULTURAL
                    }
                    else {
                        categoriaSelecionada = GeocacheType.GASTRONOMIA
                    }

                }
            )

            Spacer(Modifier.height(16.dp))

            // Campo para Localização
            LocalizacaoField(
                localizacao = localizacao,
                onLocalizacaoChange = { localizacao = it }
            )

            Spacer(Modifier.height(16.dp))

        Button(onClick = { // Criar um novo Geocache
            val geocache = Geocache(
                geocacheId = 0,
                location = localizacao,
                type = categoriaSelecionada,
                name = "", // você precisa adicionar um campo para o nome do geocache
                createdAt = LocalDateTime.now(),
                createdBy = 0 // você precisa adicionar um campo para o criador do geocache
            )
            viewModel.insertGeocache(geocache)

            // associar as hints com o geocacheId
            hints.forEach { hint ->
                hint.geocacheId = geocache.geocacheId
                viewModel.insertHint(hint)
            }

            // associar as challenges com o geocacheId
            challenges.forEach { challenge ->
                challenge.geocacheId = geocache.geocacheId
                viewModel.insertChallenge(challenge)
            }
        }) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun LabelDica(label: String, isRemovable: Boolean, onRemoveClick: () -> Unit, onLabelChange: (String) -> Unit = {}) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label:")
            Spacer(Modifier.padding(5.dp))
        }
        Spacer(Modifier.padding(5.dp))
        // Exibe o botão de remoção apenas se a dica for removível
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextField(label, onValueChange = onLabelChange)
            if (isRemovable) {
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover Dica",
                        tint = Color.Red
                    )
                }
            }

        }
    }
}

@Composable
fun LabelPergunta(label: String, onLabelChange: (String) -> Unit = {}, onAnswerChange: (String)  -> Unit = {}) {
    Column {
        Text(text = "$label:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(label, onValueChange = onLabelChange)
        Spacer(Modifier.padding(5.dp))
        Text("Atribua uma Resposta:")
        Spacer(Modifier.padding(5.dp))
        MyTextField("Resposta", onValueChange = onAnswerChange)
    }
}

@Composable
fun CategoriaDropdown(
    categorias: List<String>,
    categoriaSelecionada: String,
    onCategoriaSelecionada: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(text = "Categoria:")
        Spacer(Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .clickable(
                    onClick = { expanded = true },
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                )
        ) {
            Text(
                text = categoriaSelecionada,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(onClick = {
                        onCategoriaSelecionada(categoria)
                        expanded = false
                    }, text = { Text(text = categoria) })
                }
            }
        }
    }
}

@Composable
fun LocalizacaoField(localizacao: Location, onLocalizacaoChange: (Location) -> Unit) {
    Column {
        Text(text = "Latitude:")
        Spacer(Modifier.height(5.dp))
        TextField(
            value = localizacao.latitude.toString(),
            onValueChange = { onLocalizacaoChange(localizacao.copy(latitude = it.toDouble())) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Digite a latitude") }
        )
        Spacer(Modifier.height(10.dp))
        Text(text = "Longitude:")
        Spacer(Modifier.height(5.dp))
        TextField(
            value = localizacao.longitude.toString(),
            onValueChange = { onLocalizacaoChange(localizacao.copy(longitude = it.toDouble())) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Digite a longitude") }
        )
        Spacer(Modifier.height(10.dp))
        Text(text = "Endereço:")
        Spacer(Modifier.height(5.dp))
        TextField(
            value = localizacao.address?: "",
            onValueChange = { onLocalizacaoChange(localizacao.copy(address = it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Digite o endereço") }
        )
    }
}

@Preview
@Composable
fun CreateGeocacheScreenPreview() {
    val navController = rememberNavController()
    val viewModel: GeocacheViewsModels = viewModel()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                CreateGeocacheScreen(navController, viewModel)
            }
        }
    }
}