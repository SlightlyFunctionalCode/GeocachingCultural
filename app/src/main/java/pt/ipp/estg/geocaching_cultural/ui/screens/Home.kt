package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.HomePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.HorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallHorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun HomeScreen(navController: NavHostController) {
    var answerEmail by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var suportingTextEmail by remember { mutableStateOf("") }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
    ) {
        HomePicture(
            modifier = Modifier
        )

        VerticalSpacer()
        Text(
            text = "Desafie-se a explorar, a aprender e a criar!",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        VerticalSpacer()
        Text(
            text = "Junte-se à aventura do Geocaching Cultural e descubra os tesouros escondidos " +
                    "em locais históricos e culturais próximos a você!",
            textAlign = TextAlign.Center,
            color = LightGray
        )

        VerticalSpacer()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight()
        ) {
            MyTextField(
                label = { Text("Email*") },
                value = answerEmail,
                onValueChange = {
                    answerEmail = it
                    isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email Icon"
                    )
                },
                isError = !isEmailValid,
                supportingText = { Text(text = suportingTextEmail, color = Pink) },
                modifier = Modifier
                    .weight(2f)
            )

            suportingTextEmail = if (!isEmailValid) "Insira um email válido" else ""

            SmallHorizontalSpacer()

            MyTextButton(
                text = "Sign-Up",
                onClick = {
                    navController.navigate("registerScreen/$answerEmail")
                },
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    val navController = rememberNavController()

    Geocaching_CulturalTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            HomeScreen(navController)
        }
    }
}