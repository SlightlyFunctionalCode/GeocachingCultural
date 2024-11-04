package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.utils.ProfilePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun ProfileEditingScreen(navController: NavHostController) {
    Column(modifier = Modifier.padding(28.dp)) {
        Title(text = "Editar Perfil")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer()
            ProfilePicture()
            MyTextButton(text = "Alterar Imagem")

            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                Text(text = "Nome", color = LightGray)
                MyTextField(value = "Joel Sousa de Carvalho")
                Text(text = "Email", color = LightGray)
                MyTextField(value = "joel@gmail.com")
                SmallVerticalSpacer()
                MyTextButton(text = "Change password")
            }
        }
    }
}

@Preview
@Composable
fun ProfileEditingPreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                ProfileEditingScreen(navController)
            }
        }
    }
}