package pt.ipp.estg.geocaching_cultural.ui.screens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
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
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import java.time.LocalDateTime
import pt.ipp.estg.geocaching_cultural.utils_api.fetchGeocachesImages
import pt.ipp.estg.geocaching_cultural.utils_api.fetchPlaceDetails
import pt.ipp.estg.geocaching_cultural.utils_api.fetchPlaceSuggestions
import pt.ipp.estg.geocaching_cultural.utils_api.getApiKey

@Composable
fun CreateGeocacheScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels,
    usersViewModel: UsersViewsModels
) {
    val context = LocalContext.current

    val labelQuestions = remember {
        mutableStateListOf(
            context.getString(R.string.question_5km),
            context.getString(R.string.question_1km),
            context.getString(R.string.question_500m),
            context.getString(R.string.challenge_question),
        )
    }

    var categorySelected by remember { mutableStateOf(GeocacheType.HISTORICO) }
    val hints = remember { mutableStateListOf("", "", "") }
    val questions = remember { mutableStateListOf("", "", "", "") }
    val answers = remember { mutableStateListOf("", "", "", "") }
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }
    var address by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var isNameValid by remember { mutableStateOf(true) }
    var isHintValid by remember { mutableStateOf(true) }
    var isQuestionValid by remember { mutableStateOf(true) }
    var isAnswerValid by remember { mutableStateOf(true) }
    var isAddressValid by remember { mutableStateOf(true) }


    var buttonState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(R.string.name_geocache))
        MyTextField(value = name, onValueChange = { name = it; isNameValid = it.isNotBlank() },
            isError = !isNameValid,
            supportingText = {
                Text(
                    text = if (!isNameValid) stringResource(R.string.name_error_message) else "",
                    color = Pink
                )
            })

        hints.forEachIndexed { index, hint ->
            LabelHint(text = "${stringResource(R.string.hint)} ${index + 1}",
                hint = hint, isRemovable = index >= 3,
                onRemoveClick = { hints.removeAt(index) },
                onValueChange = { hints[index] = it; isHintValid = it.isNotBlank() },
                isError = !isHintValid,
                supportingText = {
                    Text(
                        text =
                        if (!isHintValid) stringResource(R.string.hint_invalid) else "",
                        color = Pink
                    )
                })
            Spacer(Modifier.padding(5.dp))
        }

        // Botão para adicionar uma nova dica
        Button(onClick = { hints.add("${context.getString(R.string.hint)} ${hints.size + 1}"); }) {
            Text(text = stringResource(R.string.add_hint))
        }

        labelQuestions.forEachIndexed { index, question ->
            LabelQuestion(text = question,
                quest = questions[index],
                onQuestChange = { questions[index] = it },
                answer = answers.getOrElse(index) { "" },
                onAnswerChange = { answers[index] = it },
                isErrorQuestion = !isQuestionValid,
                isErrorAnswer = !isAnswerValid,
                supportingTextQuestion = {
                    Text(
                        text = if (!isQuestionValid) stringResource(R.string.question_invalid) else "",
                        color = Pink
                    )
                },
                supportingTextAnswer = {
                    Text(
                        text = if (!isAnswerValid) stringResource(R.string.answer_invalid) else "",
                        color = Pink
                    )
                }
            )
            Spacer(Modifier.padding(5.dp))
        }
        Text(stringResource(R.string.geocache_type))
        Spacer(Modifier.padding(5.dp))
        // Caixa de seleção para categoria
        CategoryLabel(categorySelected = categorySelected,
            onCategorySelectedChange = { newCategory ->
                categorySelected = newCategory
            })

        Spacer(Modifier.height(16.dp))

        // Campo para Localização
        LocationField(
            address = address,
            onAddressChange = { address = it; isAddressValid = it.isNotBlank() },
            onPlaceSelected = { place ->
                address = place.address ?: ""
                latitude = place.latLng?.latitude ?: 0.0
                longitude = place.latLng?.longitude ?: 0.0
            },
            isError = !isAddressValid,
            supportingText = {
                Text(
                    text = if (!isAddressValid) stringResource(R.string.address_invalid) else "",
                    color = Pink
                )
            }
        )

        Spacer(Modifier.height(16.dp))
        val images = fetchGeocachesImages(latitude, longitude, context)
        // Mostrar a seleção de imagens somente quando o campo de endereço estiver preenchido
        if (address.isNotEmpty()) {
            Text(stringResource(R.string.choose_image))
            ChooseImage(images = images) { image ->
                selectedImage = image
            }
        }

        var isHintsEmpty = hints.any { it == "" }
        var isQuestionsEmpty = hints.any { it == "" }
        var isAnswersEmpty = hints.any { it == "" }

        buttonState = isNameValid &&
                isHintValid &&
                isQuestionValid &&
                isAnswerValid &&
                isAddressValid &&
                name != "" &&
                !isHintsEmpty &&
                !isQuestionsEmpty &&
                !isAnswersEmpty &&
                address != ""
        Spacer(Modifier.height(16.dp))

        MyTextButton(text = stringResource(R.string.submit),
            enabled = buttonState,
            onClick = { // Criar um novo Geocache
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
                    createdBy = usersViewModel.currentUser.value?.userId ?: 0
                )

                var geocacheId = 0
                geocacheViewsModels.insertGeocache(geocache).observeForever { id ->
                    geocacheId = id.toInt()

                    // associar as hints com o geocacheId
                    hints.forEach { hint ->
                        if (hints.isNotEmpty()) {
                            val newHint = Hint(0, geocacheId, hint)
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
                            geocacheViewsModels.insertChallenge(newChallenge)
                        }
                    }
                }

                navController.navigate("homeScreen")
            })
    }
}


@Composable
fun LabelHint(
    text: String,
    hint: String,
    isRemovable: Boolean,
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {},
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
            MyTextField(
                value = hint,
                onValueChange = onValueChange,
                isError = isError,
                supportingText = supportingText
            )
            if (isRemovable) {
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.remove_hint),
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
    onAnswerChange: (String) -> Unit,
    isErrorQuestion: Boolean = false,
    isErrorAnswer: Boolean = false,
    supportingTextQuestion: @Composable () -> Unit = {},
    supportingTextAnswer: @Composable () -> Unit = {}
) {
    Column {
        Text(text = "$text:")
        Spacer(Modifier.padding(5.dp))
        MyTextField(
            value = quest,
            onValueChange = onQuestChange,
            isError = isErrorQuestion,
            supportingText = supportingTextQuestion
        )
        Spacer(Modifier.padding(5.dp))
        Text(stringResource(R.string.add_answer))
        Spacer(Modifier.padding(5.dp))
        MyTextField(
            label = { Text(stringResource(R.string.answer_label)) },
            value = answer,
            onValueChange = onAnswerChange,
            isError = isErrorAnswer,
            supportingText = supportingTextAnswer
        )
    }
}

@Composable
fun LocationField(
    address: String,
    onAddressChange: (String) -> Unit,
    onPlaceSelected: (Place) -> Unit,
    isError: Boolean = false,
    supportingText: @Composable () -> Unit = {},
) {
    val context = LocalContext.current

    getApiKey(context)?.let { Places.initialize(context, it) }

    val placesClient = remember { Places.createClient(context) }
    val suggestions = remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    val isEditing = remember { mutableStateOf(false) }

    Column {
        Text(text = stringResource(R.string.address))
        Spacer(Modifier.height(5.dp))

        MyTextField(
            value = address, onValueChange = {
                isEditing.value = true
                onAddressChange(it)
                fetchPlaceSuggestions(it, placesClient) { newSuggestions ->
                    suggestions.value = newSuggestions
                }
            }, modifier = Modifier.fillMaxWidth(),
            isError = isError,
            supportingText = supportingText
        )

        // Exibe sugestões de auto-complete
        if (isEditing.value && suggestions.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(10.dp))
                    .height(200.dp)
            ) {
                items(suggestions.value) { suggestion ->
                    Text(text = suggestion.getFullText(null).toString(), modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Buscar os detalhes do local selecionado
                            fetchPlaceDetails(suggestion.placeId, placesClient) { place ->
                                onPlaceSelected(place)
                            }
                            suggestions.value = emptyList()
                        }
                        .padding(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseImage(images: List<ImageBitmap>, onImageSelected: (ImageBitmap) -> Unit) {
    FlowRow(
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        var imageSelected by remember { mutableStateOf<ImageBitmap?>(null) }
        images.forEach { imageBitmap ->

            Image(
                painter = BitmapPainter(imageBitmap),
                contentDescription = stringResource(R.string.choose_image),
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .sizeIn(50.dp, 50.dp, 100.dp, 100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable {
                        imageSelected = imageBitmap
                        onImageSelected(imageSelected!!)
                    }
                    .then(
                        if (imageBitmap == imageSelected) {
                            Modifier.border(
                                width = 5.dp, color = Black, shape = RoundedCornerShape(10.dp)
                            )
                        } else {
                            Modifier
                        }
                    ))
        }
    }
}


@Preview
@Composable
fun CreateGeocacheScreenPreview() {
    val context = LocalContext.current

    val labelQuestions = listOf(
        context.getString(R.string.question_5km),
        context.getString(R.string.question_1km),
        context.getString(R.string.question_500m),
        context.getString(R.string.challenge_question),
    )

    // Estado para a categoria selecionada e localização
    val categorySelected = GeocacheType.HISTORICO
    val hints = listOf("Dica1", "Dica2", "Dica3")
    val questions = listOf("Pergunta1", "Pergunta2", "Pergunta3", "Pergunta4")
    val answers = listOf("Resposta1", "Resposta2", "Resposta3", "Resposta4")
    val suggestions = listOf("Resposta1", "Resposta2", "Resposta3", "Resposta4")
    val address = "address"
    val name = "name"

    Geocaching_CulturalTheme {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    MyTextField(value = name, onValueChange = { })

                    hints.forEachIndexed { index, hint ->
                        LabelHint(text = "${stringResource(R.string.hint)} ${index + 1}",
                            hint = hint,
                            isRemovable = index >= 3,
                            onRemoveClick = { },
                            onValueChange = { })
                        Spacer(Modifier.padding(5.dp))
                    }
                    // Botão para adicionar uma nova dica
                    Button(onClick = { }) {
                        Text(text = stringResource(R.string.add_hint))
                    }

                    labelQuestions.forEachIndexed { index, question ->
                        LabelQuestion(text = question,
                            quest = questions[index],
                            onQuestChange = { },
                            answer = answers.getOrElse(index) { "" },
                            onAnswerChange = { })
                        Spacer(Modifier.padding(5.dp))
                    }
                    Text(stringResource(R.string.geocache_type))
                    Spacer(Modifier.padding(5.dp))
                    // Caixa de seleção para categoria
                    CategoryLabel(categorySelected = categorySelected,
                        onCategorySelectedChange = {})

                    Spacer(Modifier.height(16.dp))

                    // Campo para Localização
                    Column {
                        Text(text = stringResource(R.string.address))
                        Spacer(Modifier.height(5.dp))

                        MyTextField(value = address,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(stringResource(R.string.add_place)) })

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(10.dp))
                                .height(200.dp)
                        ) {
                            items(suggestions) { suggestion ->
                                Text(text = suggestion,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {}
                                        .padding(8.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    val images = listOf(
                        ImageBitmap.imageResource(id = R.drawable.avatar_male_01),
                        ImageBitmap.imageResource(id = R.drawable.avatar_male_02),
                        ImageBitmap.imageResource(id = R.drawable.avatar_male_03)
                    )

                    Text(text = stringResource(R.string.choose_image))
                    ChooseImage(images = images) { }


                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {}) {
                        Text(text = stringResource(R.string.submit))
                    }
                }
            }
        }
    }
}