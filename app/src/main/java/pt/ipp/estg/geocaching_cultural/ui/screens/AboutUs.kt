package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.HomePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun AboutUsScreen(navController: NavHostController, currentUserId: Int?) {
    var answerEmail by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var supportingTextEmail by remember { mutableStateOf("") }

    if (currentUserId != null && currentUserId != -1) {
        navController.navigate("homeScreen")
    }

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
            text = stringResource(R.string.slogan),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        VerticalSpacer()
        Text(
            text = stringResource(R.string.about_us),
            textAlign = TextAlign.Center,
            color = LightGray
        )

        /* TODO: Colocar tamanho do layout Dinâmico para impedir o botão de sair do ecrã */
        SmallVerticalSpacer()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MyTextField(
                label = {
                    Text(
                        stringResource(R.string.email_label)
                    )
                },
                value = answerEmail,
                onValueChange = {
                    answerEmail = it
                    isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(R.string.email_icon)
                    )
                },
                isError = !isEmailValid,
                supportingText = { Text(text = supportingTextEmail, color = Pink) },
                modifier = Modifier
            )

            supportingTextEmail =
                if (!isEmailValid) stringResource(R.string.email_error_message) else ""

            MyTextButton(
                text = "Sign-Up",
                onClick = {
                    navController.navigate("registerScreen/$answerEmail")
                },
                modifier = Modifier
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
            AboutUsScreen(navController, 1)
        }
    }
}