package pt.ipp.estg.geocaching_cultural.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Challenge
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.GeocacheWithHintsAndChallenges
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
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
import java.time.LocalDateTime

@Composable
fun CreatedGeocacheDetailsScreen(
    navController: NavHostController,
    geocacheViewsModels: GeocacheViewsModels,
    geocacheId: Int?
) {
    val geocacheLiveData = geocacheViewsModels.getGeocacheWithHintsAndChallenges(geocacheId ?: 0)
    val geocache by geocacheLiveData.observeAsState()

    val context = LocalContext.current

    Column(Modifier.padding(top = 28.dp, start = 28.dp, end = 28.dp, bottom = 0.dp)) {

        if (geocache != null) {
            data class QuestionAndAnswer(
                val label: String,
                val question: String,
                val answer: String
            )

            val questions = remember {
                mutableStateListOf(
                    QuestionAndAnswer(
                        context.getString(R.string.question_5km),
                        geocache!!.challenges[0].question,
                        geocache!!.challenges[0].correctAnswer
                    ),
                    QuestionAndAnswer(
                        context.getString(R.string.question_1km),
                        geocache!!.challenges[1].question,
                        geocache!!.challenges[1].correctAnswer
                    ),
                    QuestionAndAnswer(
                        context.getString(R.string.question_500m),
                        geocache!!.challenges[2].question,
                        geocache!!.challenges[2].correctAnswer
                    ),
                    QuestionAndAnswer(
                        context.getString(R.string.challenge_question),
                        geocache!!.challenges[3].question,
                        geocache!!.challenges[3].correctAnswer
                    )
                )
            }

            val geocacheType = remember { mutableStateOf(geocache!!.geocache.type) }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1000.dp)
            ) {
                item {
                    Title(text = stringResource(R.string.geocache_details))
                    Column {
                        VerticalSpacer()

                        Text(
                            text = stringResource(R.string.hints),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        geocache!!.hints.forEachIndexed { index, hint ->
                            HintItem("${stringResource(R.string.hint)} ${index + 1}", hint.hint)
                            SmallVerticalSpacer()
                        }

                        VerticalSpacer()

                        Text(
                            text = stringResource(R.string.challenges),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        for (questionAndAnswer in questions) {
                            QuestionItem(
                                questionAndAnswer.label,
                                questionAndAnswer.question,
                                questionAndAnswer.answer
                            )
                            SmallVerticalSpacer()
                        }

                        VerticalSpacer()
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.location),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    SmallHorizontalSpacer()
                    LocationItem(geocache!!.geocache.address)

                    VerticalSpacer()

                    Text(
                        text = stringResource(R.string.geocache_type),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    SmallHorizontalSpacer()
                    CategoryItem(geocacheType.value)
                    VerticalSpacer()
                }
            }
        } else {
            Text(stringResource(R.string.loading_geocache_info))
        }
    }
}

@Composable
fun HintItem(label: String, hint: String) {
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
                Text(text = label)
                Text(text = hint)
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
                contentDescription = stringResource(R.string.hint_icon),
                modifier = Modifier.size(20.dp)
            )
            SmallHorizontalSpacer()
            Column {
                Text(text = label)
                Text(text = question)
                Text(
                    text = "${stringResource(R.string.answer_question)} $answer",
                    color = LightGray,
                    fontSize = 12.sp
                )
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
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = stringResource(R.string.location_icon)
            )
            SmallHorizontalSpacer()
            Column {
                Text(
                    text = "${stringResource(R.string.address)} $address",
                    color = LightGray,
                    fontSize = 12.sp
                )
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
            Icon(
                painter = when (category) {
                    GeocacheType.GASTRONOMIA -> painterResource(R.drawable.gastronomia)
                    GeocacheType.CULTURAL -> painterResource(R.drawable.cultural)
                    GeocacheType.HISTORICO -> painterResource(R.drawable.historico)
                },
                contentDescription = stringResource(R.string.show_categories_icon),
                modifier = Modifier
                    .size(24.dp)
            )
            SmallHorizontalSpacer()
            Text(text = category.name)
        }
    }
}

@Preview
@Composable
fun CreatedGeocacheDetailsScreenPreview() {
    val avatar = ImageBitmap.imageResource(id = R.drawable.home)

    val geocache =
        GeocacheWithHintsAndChallenges(
            geocache = Geocache(
                1,
                Location(0.0, 0.0),
                GeocacheType.HISTORICO,
                "Address 1",
                "ImageURL1",
                avatar,
                LocalDateTime.now(),
                1
            ), hints =
            listOf(
                Hint(1, 1, "Hint 1"),
                Hint(2, 1, "Hint 2"),
                Hint(3, 1, "Hint 3")
            ),
            challenges = listOf(
                Challenge(1, 1, "Challenge 1", "answer challenge 1", 7),
                Challenge(2, 1, "Challenge 2", "answer challenge 2", 7),
                Challenge(3, 1, "Challenge 3", "answer challenge 3", 7),
                Challenge(3, 1, "Final Challenge", "answer final challenge", 7)
            )
        )

    val context = LocalContext.current

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                data class QuestionAndAnswer(
                    val label: String,
                    val question: String,
                    val answer: String
                )

                val questions = remember {
                    mutableStateListOf(
                        QuestionAndAnswer(
                            context.getString(R.string.question_5km),
                            geocache.challenges[0].question,
                            geocache.challenges[0].correctAnswer
                        ),
                        QuestionAndAnswer(
                            context.getString(R.string.question_1km),
                            geocache.challenges[1].question,
                            geocache.challenges[1].correctAnswer
                        ),
                        QuestionAndAnswer(
                            context.getString(R.string.question_500m),
                            geocache.challenges[2].question,
                            geocache.challenges[2].correctAnswer
                        ),
                        QuestionAndAnswer(
                            context.getString(R.string.challenge_question),
                            geocache.challenges[3].question,
                            geocache.challenges[3].correctAnswer
                        )
                    )
                }

                val geocacheType = remember { mutableStateOf(geocache.geocache.type) }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1000.dp)
                ) {
                    item {
                        Title(text = stringResource(R.string.geocache_details))
                        Column {
                            VerticalSpacer()

                            Text(
                                text = stringResource(R.string.hints),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            geocache.hints.forEachIndexed { index, hint ->
                                HintItem("${stringResource(R.string.hint)} ${index + 1}", hint.hint)
                                SmallVerticalSpacer()
                            }

                            VerticalSpacer()

                            Text(
                                text = stringResource(R.string.challenges),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            for (questionAndAnswer in questions) {
                                QuestionItem(
                                    questionAndAnswer.label,
                                    questionAndAnswer.question,
                                    questionAndAnswer.answer
                                )
                                SmallVerticalSpacer()
                            }

                            VerticalSpacer()
                        }
                    }
                    item {
                        Text(
                            text = stringResource(R.string.location),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        SmallHorizontalSpacer()
                        LocationItem(geocache.geocache.address)

                        VerticalSpacer()

                        Text(
                            text = stringResource(R.string.geocache_type),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        SmallHorizontalSpacer()
                        CategoryItem(geocacheType.value)
                    }
                }
            }
        }
    }
}