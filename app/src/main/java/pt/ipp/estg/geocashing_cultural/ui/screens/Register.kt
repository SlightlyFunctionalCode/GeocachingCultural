package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow
import pt.ipp.estg.geocashing_cultural.ui.utils.HorizontalSpacer
import pt.ipp.estg.geocashing_cultural.ui.utils.LargeVerticalSpacer
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer

@Composable
fun RegisterScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.65f)
                .align(Alignment.Center)
        ) {
            item {
                Text(
                    text = "Registo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }

            item {
                VerticalSpacer()
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Name", color = LightGray)
                        MyTextField(
                            value = "Joni Smith"
                        )
                    }
                    HorizontalSpacer()
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Email", color = LightGray)
                        MyTextField(
                            value = "joni@gmail.com"
                        )
                    }
                }
            }

            item {
                VerticalSpacer()
                Column() {
                    Text(text = "Password", color = LightGray)
                    MyTextField(value = "*********", modifier = Modifier.fillMaxWidth())
                }
            }

            item {
                VerticalSpacer()
                Row {
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "Open Navigation Items",
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "Open Navigation Items",
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "Open Navigation Items",
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
            }

            item {
                VerticalSpacer()
                MyTextButton(
                    text = "Submit",
                    onClick = { navController.navigate("loginScreen") },
                    modifier = Modifier.fillMaxWidth()
                )
                LargeVerticalSpacer()
            }
        }
    }
}

@Preview
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()

    Geocashing_CulturalTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            RegisterScreen(navController)
        }
    }
}