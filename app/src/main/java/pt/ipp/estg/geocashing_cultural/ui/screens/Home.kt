package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray
import pt.ipp.estg.geocashing_cultural.ui.theme.White

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.5f)
                .rotate(-3f)
                .background(
                    color = White,
                    shape = RoundedCornerShape(40.dp)
                )
                .align(Alignment.CenterHorizontally)
                .padding(15.dp)
        ) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Home Picture"
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Desafie-se a explorar, a aprender e a criar!",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Junte-se à aventura do Geocaching Cultural e descubra os tesouros escondidos " +
                    "em locais históricos e culturais próximos a você!",
            textAlign = TextAlign.Center,
            color = LightGray
        )
        Spacer(modifier = Modifier.height(24.dp))
        var email by remember { mutableStateOf("joni@gmail.com") }
        Row() {
            MyTextField(
                "joni@gmail.com", {},
                modifier = Modifier.weight(2f)
            )

            Spacer(modifier = Modifier.width(24.dp))

            MyTextButton(
                text = "Sign-Up",
                onClick = {
                    navController.navigate("registerScreen")
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)

            )
        }
    }
}