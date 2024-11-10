package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.HorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.PointsDisplay
import pt.ipp.estg.geocaching_cultural.ui.utils.ProfilePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallHorizontalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun ProfileScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val userState = usersViewsModels.currentUser.observeAsState()

    userState.value?.let { user ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Title(text = "Perfil", modifier = Modifier.weight(1.25f))
                HorizontalSpacer()
                PointsDisplay(points = user.points, modifier = Modifier.weight(1f))
            }

            VerticalSpacer()
            ProfilePicture(user.profileImageUrl)


            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                Text(text = user.name, fontSize = 24.sp)

                Text(text = user.email, color = LightGray)
                SmallVerticalSpacer()
                MyTextButton(text = "Editar Informações de Perfil",
                    { navController.navigate("profileEditingScreen") })
            }


            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                Text(text = "Ações Adicionais", fontSize = 24.sp, color = Pink)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyTextButton(
                        text = "Eliminar Perfil",
                        onClick = {
                            usersViewsModels.deleteUser(user)
                            usersViewsModels.saveCurrentUserId(-1)
                            navController.navigate("aboutUsScreen")
                        }
                    )
                    SmallHorizontalSpacer()
                    Text(text = "*Ação Irreversível", color = Pink, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                //  ProfileScreen(navController)
            }
        }
    }
}