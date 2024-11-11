package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Black
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.utils.ProfilePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChooseProfilePicScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val currentUser = usersViewsModels.currentUser.observeAsState()

    val defaultProfilePics = listOf(
        R.drawable.avatar_male_01,
        R.drawable.avatar_male_02,
        R.drawable.avatar_male_03,
        R.drawable.avatar_female_01,
        R.drawable.avatar_female_02,
        R.drawable.avatar_female_03
    )

    var answerProfilePicUrl by remember { mutableStateOf(currentUser.value?.profileImageUrl ?: "") }
    var answerProfilePicDefault by remember {
        mutableIntStateOf(
            currentUser.value?.profilePictureDefault ?: R.drawable.avatar_male_01
        )
    }

    var buttonState by remember { mutableStateOf(false) }

    var isProfilePicUrlValid by remember { mutableStateOf(true) }

    /*TODO: add errors */
    var supportingTextProfilePicUrlValid by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var updateSuccessful by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(28.dp)) {
        Title(text = "Alterar Imagem de Perfil")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Foto Perfil Atual")

            ProfilePicture(
                currentUser.value?.profileImageUrl,
                currentUser.value?.profilePictureDefault
            )

            VerticalSpacer()

            MyTextButton(text = "Remover Imagem de Perfil Custom", { answerProfilePicUrl = "" })

            VerticalSpacer()

            Column {
                MyTextField(
                    label = { Text("Choose Custom Profile Picture Url") },
                    value = answerProfilePicUrl,
                    onValueChange = {
                        answerProfilePicUrl = it
                        isProfilePicUrlValid = android.util.Patterns.WEB_URL.matcher(it).matches()
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = "Lock Icon"
                        )
                    },
                    isError = !isProfilePicUrlValid,
                    supportingText = { Text(text = supportingTextProfilePicUrlValid, color = Pink) },
                    modifier = Modifier
                )

                supportingTextProfilePicUrlValid =
                    if (!isProfilePicUrlValid) "Senha deve ter pelo menos 6 caracteres" else ""

                VerticalSpacer()

                FlowRow(
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                     modifier = Modifier.fillMaxWidth()
                ) {
                    for (image in defaultProfilePics)
                        Image(
                            painter = painterResource(image),
                            contentDescription = "Default avatar pic",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .padding(8.dp)
                                .sizeIn(50.dp, 50.dp, 100.dp, 100.dp)
                                .clickable { answerProfilePicDefault = image }
                                .then(
                                    if (image == answerProfilePicDefault) {
                                        Modifier.border(
                                            width = 5.dp,
                                            color = Black,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                    } else {
                                        Modifier // No border modifier
                                    }
                                )
                        )
                }
            }

            buttonState = isProfilePicUrlValid

            VerticalSpacer()
            MyTextButton(
                text = "Submit",
                enabled = buttonState && !updateSuccessful,
                onClick = {
                    currentUser.value?.let {
                        updateUser(
                            it,
                            answerProfilePicUrl,
                            answerProfilePicDefault,
                            usersViewsModels,
                            onUpdateOutcome = { outcome ->
                                if (outcome) {
                                    updateSuccessful = true
                                }
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            LaunchedEffect(updateSuccessful) {
                if (updateSuccessful) {
                    snackbarHostState.showSnackbar("Perfil Atualizado!")
                    navController.navigate("profileScreen")
                    updateSuccessful = false
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
            )
        }
    }
}


private fun updateUser(
    user: User,
    profilePicUrl: String,
    profilePicDefault: Int,
    usersViewsModels: UsersViewsModels,
    onUpdateOutcome: (Boolean) -> Unit
) {
    val updatedUser = User(
        userId = user.userId,
        name = user.name,
        email = user.email,
        password = user.password,
        points = user.points,
        profileImageUrl = profilePicUrl,
        profilePictureDefault = profilePicDefault,
        location = user.location,
    )
    usersViewsModels.updateUser(updatedUser)
    onUpdateOutcome(true)
}

