package pt.ipp.estg.geocaching_cultural.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Black
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import java.time.LocalDateTime
import pt.ipp.estg.geocaching_cultural.utils_api.fetchAddressSuggestions
import pt.ipp.estg.geocaching_cultural.utils_api.fetchCoordinatesFromAddress
import pt.ipp.estg.geocaching_cultural.utils_api.fetchGeocachesImages
import pt.ipp.estg.geocaching_cultural.utils_api.fetchNearbyImages
import pt.ipp.estg.geocaching_cultural.utils_api.getApiKey

@Composable
fun CreateGeocacheScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels,
    usersViewModel: UsersViewsModels
) {
    val labelQuestions = remember {
        mutableStateListOf(
            "Pergunta para 5km",
            "Pergunta para 1km",
            "Pergunta para 500m",
            "Pergunta Final"
        )
    }
    // Estado para a categoria selecionada e localização
    var categorySelected by remember { mutableStateOf(GeocacheType.HISTORICO) }
    val hints = remember { mutableStateListOf("", "", "") }
    val questions = remember { mutableStateListOf("", "", "", "") }
    val answers = remember { mutableStateListOf("", "", "", "") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var address by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        MyTextField(value = name, onValueChange = { name = it })

        hints.forEachIndexed { index, hint ->
            LabelHint(text = "Dica ${index + 1}",
                hint = hint, isRemovable = index >= 3,
                onRemoveClick = { hints.removeAt(index) },
                onValueChange = { hints[index] = it }
            )
            Spacer(Modifier.padding(5.dp))
        }
        // Botão para adicionar uma nova dica
        Button(onClick = { hints.add("Dica ${hints.size + 1}"); }) {
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
            onCategorySelectedChange = { newCategory ->
                categorySelected = newCategory
            })

        Spacer(Modifier.height(16.dp))

        // Campo para Localização
        LocationField(
            address = address,
            onAddressChange = { address = it },
            onAddressSelected = { selectedAddress ->
                address = selectedAddress
                val apiKey = getApiKey(context)
                CoroutineScope(Dispatchers.Main).launch {
                    if (apiKey != null) {
                        val coordinates = fetchCoordinatesFromAddress(selectedAddress, apiKey)

                        if (coordinates?.first != null && coordinates.second != null) {
                            latitude = coordinates.first!!
                            longitude = coordinates.second!!
                        } else {
                            Log.e("Geocoding", "Falha ao obter coordenadas para o endereço fornecido")
                        }
                    } else {
                        Log.e("Geocoding", "Chave de API não encontrada")
                    }
                }
            }
        )

        Spacer(Modifier.height(16.dp))
        val images = fetchGeocachesImages(latitude, longitude, context)
        // Mostrar a seleção de imagens somente quando o campo de endereço estiver preenchido
        if (address.isNotEmpty()) {
            Text("Escolha uma imagem:")
            ChooseImage(images = images) { image ->
                selectedImage = image // Armazena a imagem selecionada
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { // Criar um novo Geocache
            val geocache = Geocache(
                geocacheId = 0,
                address = address,
                location = Location(
                    lat = latitude,
                    lng = longitude,
                ),
                type = categorySelected,
                name = name,
                image = selectedImage!!,
                createdAt = LocalDateTime.now(),
                createdBy = usersViewModel.currentUser.value?.userId
                    ?: 0
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
                            pointsAwarded = 0
                        )
                        Log.d("Debug", "Challenge criado: $newChallenge")
                        geocacheViewsModels.insertChallenge(newChallenge)
                    }
                }
            }
            geocacheViewsModels.getGeocacheWithHintsAndChallenges(1).observeForever { geocache ->
                Log.d(
                    "Debug",
                    "Geocache data: ${geocache.toString()}"
                )
            }
            navController.navigate("homeScreen")
        }) {
            Text(text = "Enviar")
        }
    }
}


@Composable
fun LabelHint(
    text: String,
    hint: String,
    isRemovable: Boolean,
    onRemoveClick: () -> Unit,
    onValueChange: (String) -> Unit = {}
) {
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
fun LabelQuestion(
    text: String,
    quest: (String),
    onQuestChange: (String) -> Unit,
    answer: String,
    onAnswerChange: (String) -> Unit
) {
    Column {
        Text(text = "$text:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(value = quest, onValueChange = onQuestChange)
        Spacer(Modifier.padding(5.dp))
        Text("Atribua uma Resposta:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(label = { Text("Resposta") }, value = answer, onValueChange = onAnswerChange)
    }
}

@Composable
fun LocationField(
    address: String,
    onAddressChange: (String) -> Unit,
    onAddressSelected: (String) -> Unit
) {
    val context = LocalContext.current

    getApiKey(context)?.let { Places.initialize(context, it) }

    val placesClient = remember { Places.createClient(context) }
    val suggestions = remember { mutableStateOf<List<String>>(emptyList()) }

    Column {
        Text(text = "Endereço:")
        Spacer(Modifier.height(5.dp))

        MyTextField(
            value = address,
            onValueChange = {
                onAddressChange(it)
                fetchAddressSuggestions(it, placesClient) { suggestions.value = it }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Digite o endereço") }
        )

        // Exibe sugestões de auto-complete
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Defina uma altura máxima para o LazyColumn
        ) {
            items(suggestions.value) { suggestion ->
                Text(
                    text = suggestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddressSelected(suggestion)
                            suggestions.value = emptyList() // Limpar sugestões após selecionar
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseImage(images:List<ImageBitmap>, onImageSelected: (ImageBitmap) -> Unit) {
    FlowRow(
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        images.forEach { imageBitmap ->

                Image(
                    painter = BitmapPainter(imageBitmap),
                    contentDescription = "Escolha de imagem",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .padding(8.dp)
                        .sizeIn(50.dp, 50.dp, 100.dp, 100.dp)
                        .clickable {
                            onImageSelected(imageBitmap) // Retorna o Bitmap selecionado
                        }
                )
            }
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