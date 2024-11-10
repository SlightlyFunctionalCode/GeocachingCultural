package pt.ipp.estg.geocaching_cultural.ui.screens

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
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
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import java.time.LocalDateTime

@Composable
fun CreateGeocacheScreen(navController: NavHostController, geocacheViewsModels: GeocacheViewsModels, usersViewModel: UsersViewsModels) {
    val labelQuestions = remember {mutableStateListOf("Pergunta para 5km", "Pergunta para 1km", "Pergunta para 500m", "Pergunta Final")}
    // Estado para a categoria selecionada e localização
    var categorySelected by remember { mutableStateOf(GeocacheType.HISTORICO) }
    var location by remember { mutableStateOf(Location(0.0, 0.0, "")) }
    val hints = remember { mutableStateListOf("", "", "") }
    val questions = remember { mutableStateListOf("", "", "", "") }
    val answers = remember { mutableStateListOf("", "", "", "") }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        hints.forEachIndexed { index, hint ->
            LabelHint(text = "Dica ${index + 1}",
                hint = hint, isRemovable = index >= 3,
                onRemoveClick = { hints.removeAt(index) },
                onValueChange = { hints[index] = it }
            )
            Spacer(Modifier.padding(5.dp))
        }
        // Botão para adicionar uma nova dica
        Button(onClick = { hints.add("Dica ${hints.size + 1}");}) {
            Text(text = "Adicionar Dica")
        }

        labelQuestions.forEachIndexed { index, question ->
            LabelQuestion(
                text = question,
                quest = questions[index],
                onQuestChange = { questions[index] = it },
                answer = answers.getOrElse(index) { "" },
                onAnswerChange = { answers[index] = it }
            )
            Spacer(Modifier.padding(5.dp))
        }
        Text("Categoria:")
        Spacer(Modifier.padding(5.dp))
        // Caixa de seleção para categoria
        CategoryLabel(
            categorySelected = categorySelected,
            onCategorySelectedChange = { newCategory->
                categorySelected = newCategory
            })

        Spacer(Modifier.height(16.dp))

        // Campo para Localização
        LocationField(
            localizacao = location,
            onLocalizacaoChange = { location = it }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = { // Criar um novo Geocache
            val geocache = Geocache(
                geocacheId = 0,
                location = location,
                type = categorySelected,
                name = "", // você precisa adicionar um campo para o nome do geocache
                createdAt = LocalDateTime.now(),
                createdBy = usersViewModel.currentUser.value?.userId?: 0 // você precisa adicionar um campo para o criador do geocache
            )
            Log.d("Debug", "Geocache criada: $geocache")
            var geocacheId = 0
            geocacheViewsModels.insertGeocache(geocache).observeForever { id ->
                geocacheId = id.toInt()

                // associar as hints com o geocacheId
                hints.forEach { hint ->
                    if (hints.isNotEmpty()) {
                        val newHint = Hint(0, geocacheId, hint)
                        Log.d("Debug", "Hint criada: $newHint")
                        geocacheViewsModels.insertHint(newHint)
                    }
                }

                // associar as challenges com o geocacheId
                questions.forEachIndexed { index, quest ->
                    if (questions.isNotEmpty()) {
                        val newChallenge = Challenge(
                            challengeId = 0,
                            geocacheId = geocacheId,
                            question = quest,
                            correctAnswer = answers[index],
                            pointsAwarded = 0 // você precisa adicionar um campo para os pontos da challenge
                        )
                        Log.d("Debug", "Challenge criado: $newChallenge")
                        geocacheViewsModels.insertChallenge(newChallenge)
                    }
                }
            }
            geocacheViewsModels.getGeocacheWithHintsAndChallenges(1).observeForever { geocache ->
                Log.d("Debug", "Geocache data: ${geocache.toString()}") // Imprimir dados após observação
            }
            navController.navigate("homeScreen")
        }) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun LabelHint(text: String, hint: String, isRemovable: Boolean, onRemoveClick: () -> Unit, onValueChange: (String) -> Unit = {}) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$text:")
            Spacer(Modifier.padding(5.dp))
        }
        Spacer(Modifier.padding(5.dp))
        // Exibe o botão de remoção apenas se a dica for removível
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextField(value = hint, onValueChange = onValueChange)
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
fun LabelQuestion(text: String, quest: (String), onQuestChange: (String) -> Unit, answer: String, onAnswerChange: (String)  -> Unit) {
    Column {
        Text(text = "$text:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(value= quest , onValueChange = onQuestChange)
        Spacer(Modifier.padding(5.dp))
        Text("Atribua uma Resposta:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(label = { Text("Resposta")}, value = answer, onValueChange = onAnswerChange)
    }
}

@Composable
fun LocationField(localizacao: Location, onLocalizacaoChange: (Location) -> Unit) {
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
    val geocacheViewModel: GeocacheViewsModels = viewModel()
    val usersViewModel: UsersViewsModels = viewModel()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                CreateGeocacheScreen(navController, geocacheViewModel, usersViewModel)
            }
        }
    }
}