package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.White
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallHorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun CreatedGeocacheDetailsScreen(navController: NavHostController) {
    val tips = remember { mutableStateListOf("Dica 1", "Dica 2", "Dica 3") }

    data class QuestionAndAnswer(val question: String, val answer: String)

    val questions = remember {
        mutableStateListOf(
            QuestionAndAnswer("Pergunta para 5km", "Resposta para 5km"),
            QuestionAndAnswer("Pergunta para 1km", "Resposta para 1km"),
            QuestionAndAnswer("Pergunta para 500m", "Resposta para 500m"),
            QuestionAndAnswer("Pergunta Desafio", "Resposta Desafio")
        )
    }

    Column(modifier = Modifier.padding(28.dp)) {
        Title(text = "Detalhes Geocache")

        VerticalSpacer()

        Text(text = "Dicas:", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        for (tip in tips) {
            TipItem(tip)
            SmallVerticalSpacer()
        }

        VerticalSpacer()

        Text(text = "Perguntas:", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        for (questionAndAnswer in questions) {
            QuestionItem(questionAndAnswer.question, questionAndAnswer.answer)
            SmallVerticalSpacer()
        }
    }
}

@Composable
fun TipItem(tip: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Row {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "tip icon")
            SmallHorizontalSpacer()
            Text(text = tip)
        }
    }
}

@Composable
fun QuestionItem(question: String, answer: String) {
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
                Text(text = question)
                Text(text = answer, color = LightGray, fontSize = 12.sp)
            }
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
                CreatedGeocacheDetailsScreen(navController)
            }
        }
    }
}