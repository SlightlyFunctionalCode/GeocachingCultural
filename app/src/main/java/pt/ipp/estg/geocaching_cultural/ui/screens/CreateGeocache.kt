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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField

@Composable
fun CreateGeocacheScreen(navController: NavHostController) {
    // Lista de dicas inicializada com três caixas de dica
    val dicas = remember { mutableStateListOf("Dica 1", "Dica 2", "Dica 3") }
    val perguntas = remember {mutableStateListOf("Pergunta para 5km", "Pergunta para 1km", "Pergunta para 500m", "Pergunta Final")}
    // Estado para a categoria selecionada e localização
    val categorias = listOf("Histórico", "Cultural", "Gastronômico") // Lista de categorias
    var categoriaSelecionada by remember { mutableStateOf(categorias[0]) }
    var localizacao by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

            dicas.forEachIndexed { index, dica ->
                LabelDica(label = dica, isRemovable = index>=3,
                    onRemoveClick = {dicas.removeAt(index)})
                Spacer(Modifier.padding(5.dp))
            }
            // Botão para adicionar uma nova dica
            Button(onClick = { dicas.add("Dica ${dicas.size + 1}") }) {
                Text(text = "Adicionar Dica")
            }


            perguntas.forEachIndexed { index, pergunta ->
                LabelPergunta(label = pergunta)
                Spacer(Modifier.padding(5.dp))
            }


            // Caixa de seleção para categoria
            CategoriaDropdown(
                categorias = categorias,
                categoriaSelecionada = categoriaSelecionada,
                onCategoriaSelecionada = { categoriaSelecionada = it }
            )

            Spacer(Modifier.height(16.dp))

            // Campo para Localização
            LocalizacaoField(
                localizacao = localizacao,
                onLocalizacaoChange = { localizacao = it }
            )

            Spacer(Modifier.height(16.dp))


            Button(onClick = { /*TODO: Ação de envio*/ }) {
                Text(text = "Enviar")
            }
        }
    }

@Composable
fun LabelDica(label: String, isRemovable: Boolean, onRemoveClick: () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label:")
            Spacer(Modifier.padding(5.dp))
        }
        Spacer(Modifier.padding(5.dp))
        // Exibe o botão de remoção apenas se a dica for removível
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextField(label)
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
fun LabelPergunta(label: String) {
    Column {
        Text(text = "$label:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(label)
        Spacer(Modifier.padding(5.dp))
        Text("Atribua uma Resposta:")
        Spacer(Modifier.padding(5.dp))
        MyTextField("Resposta")

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
fun LocalizacaoField(localizacao: String, onLocalizacaoChange: (String) -> Unit) {
    Column {
        Text(text = "Localização:")
        Spacer(Modifier.height(5.dp))
        TextField(
            value = localizacao,
            onValueChange = onLocalizacaoChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Digite a localização") }
        )
    }
}

@Preview
@Composable
fun CreateGeocacheScreenPreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                CreateGeocacheScreen(navController)
            }
        }
    }
}