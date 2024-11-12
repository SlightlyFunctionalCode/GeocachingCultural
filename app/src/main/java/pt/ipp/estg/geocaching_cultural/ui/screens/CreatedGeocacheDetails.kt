package pt.ipp.estg.geocaching_cultural.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.White
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallHorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun CreatedGeocacheDetailsScreen(navController: NavHostController, geocacheViewsModels: GeocacheViewsModels) {
    val geocacheLiveData = geocacheViewsModels.getGeocacheWithHintsAndChallenges(2) /* TODO: deve ir buscar este id ao CreatedGeocaches */
    val geocache by geocacheLiveData.observeAsState()

    // Log para verificar se geocache não é null
    Log.d("Debug", "Geocache data: $geocache")

    if (geocache != null) {
        data class QuestionAndAnswer(val label: String, val question: String, val answer: String)
        val questions = remember {
            mutableStateListOf(
                QuestionAndAnswer("Pergunta para 5km", geocache!!.challenges[0].question, geocache!!.challenges[0].correctAnswer),
                QuestionAndAnswer("Pergunta para 1km", geocache!!.challenges[1].question, geocache!!.challenges[1].correctAnswer),
                QuestionAndAnswer("Pergunta para 500m", geocache!!.challenges[2].question, geocache!!.challenges[2].correctAnswer),
                QuestionAndAnswer("Pergunta Desafio", geocache!!.challenges[3].question, geocache!!.challenges[3].correctAnswer)
            )
        }

        val geocacheLocation = remember {
            mutableStateOf(
                Location(
                    lat = geocache!!.geocache.location.lat,
                    lng = geocache!!.geocache.location.lng
                )
            )
        }

        val geocacheType = remember { mutableStateOf(geocache!!.geocache.type) }

        LazyColumn(modifier = Modifier.fillMaxWidth().height(1000.dp)) {
            item {
                Title(text = "Detalhes Geocache")
                Column {
                    VerticalSpacer()

                    Text(text = "Dicas:", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    geocache!!.hints.forEachIndexed { index, hint ->
                        TipItem("Dica ${index + 1}", hint.hint)
                        SmallVerticalSpacer()
                    }

                    VerticalSpacer()

                    Text(text = "Perguntas:", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    for (questionAndAnswer in questions) {
                        QuestionItem(questionAndAnswer.label,questionAndAnswer.question, questionAndAnswer.answer)
                        SmallVerticalSpacer()
                    }

                    VerticalSpacer()
                }
            }
            item {
                Text(text = "Localização:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                SmallHorizontalSpacer()
                LocationItem(geocache!!.geocache.address)

                VerticalSpacer()

                Text(text = "Tipo de Geocache:", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                SmallHorizontalSpacer()
                CategoryItem(geocacheType.value)
            }
        }
    } else {
        Text("Carregando dados do geocache...")
    }
}

@Composable
fun TipItem(tip: String, dica:String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Row {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "tip icon")
            SmallHorizontalSpacer()
            Column {
                Text(text = tip)
                Text(text = dica)
            }
        }
    }
}

@Composable
fun QuestionItem(label: String, question: String, answer: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.question),
                contentDescription = "tip icon",
                modifier = Modifier.size(20.dp)
            )
            SmallHorizontalSpacer()
            Column {
                Text(text = label)
                Text(text = question)
                Text(text = "Resposta: $answer", color = LightGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun LocationItem(address: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "location icon")
            SmallHorizontalSpacer()
            Column {
                Text(text = "Endereço: $address", color = LightGray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CategoryItem(category: GeocacheType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "category icon")
            SmallHorizontalSpacer()
            Text(text = category.name)
        }
    }
}

@Preview
@Composable
fun CreatedGeocacheDetailsScreenPreview() {
    val navController = rememberNavController()

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
               // CreatedGeocacheDetailsScreen(navController)
            }
        }
    }
}