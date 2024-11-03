package pt.ipp.estg.geocashing_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow

@Composable
fun AnswerQuestionDialog() {
    var dialogOpen by remember { mutableStateOf(true) }

    if (dialogOpen) {
        Dialog(
            onDismissRequest = { dialogOpen = false },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(size = 10.dp),
                //color = LightGray
            ) {
                Column(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.question),
                            contentDescription = "tip icon",
                            modifier = Modifier.size(20.dp)
                        )
                        SmallHorizontalSpacer()
                        Text(text = "Qual é o dia de Portugal?", fontSize = 20.sp)
                    }
                    SmallVerticalSpacer()
                    MyTextField(value = "", placeholder = {
                        Text(text = "Resposta")
                    })
                    VerticalSpacer()
                    MyTextButton("Submeter", onClick = { dialogOpen = false })
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    val navController = rememberNavController()

    Geocashing_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                AnswerQuestionDialog()
            }
        }
    }
}