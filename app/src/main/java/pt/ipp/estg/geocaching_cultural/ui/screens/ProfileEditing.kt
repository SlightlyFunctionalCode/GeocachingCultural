package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.utils.ProfilePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.Title
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun ProfileEditingScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val currentUser = usersViewsModels.currentUser.observeAsState()

    var answerName by remember { mutableStateOf(currentUser.value?.name ?: "") }
    var answerEmail by remember { mutableStateOf(currentUser.value?.email ?: "") }

    var buttonState by remember { mutableStateOf(false) }

    var isEmailValid by remember { mutableStateOf(true) }
    var isNameValid by remember { mutableStateOf(true) }

    var supportingTextName by remember { mutableStateOf("") }
    var supportingTextEmail by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var updateSuccessful by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(28.dp)) {
        Title(text = "Editar Perfil")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer()
            ProfilePicture(currentUser.value?.profileImageUrl)
            /* TODO: função de alterar imagem a partir de presets ou de um link */
            MyTextButton(text = "Alterar Imagem")

            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                MyTextField(
                    label = { Text("Nome*") },
                    value = answerName,
                    onValueChange = {
                        answerName = it
                        isNameValid = it.isNotBlank()
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    isError = !isNameValid,
                    supportingText = { Text(text = supportingTextName, color = Pink) },
                    modifier = Modifier
                )
                supportingTextName = if (!isNameValid) "Nome é obrigatório" else ""

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
                    supportingText = { Text(text = supportingTextEmail, color = Pink) },
                    modifier = Modifier
                )

                supportingTextEmail = if (!isEmailValid) "Insira um email válido" else ""

                SmallVerticalSpacer()
                /* TODO: tentar fazer integraçao com o firebase para mandar email */
                MyTextButton(text = "Change password")

                buttonState = isNameValid &&
                        isEmailValid &&
                        answerName != "" &&
                        answerEmail != ""

                VerticalSpacer()
                MyTextButton(
                    text = "Submit",
                    enabled = buttonState && !updateSuccessful,
                    onClick = {
                        currentUser.value?.let {
                            updateUser(
                                it,
                                answerName,
                                answerEmail,
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
}

private fun updateUser(
    user: User,
    name: String,
    email: String,
    usersViewsModels: UsersViewsModels,
    onUpdateOutcome: (Boolean) -> Unit
) {
    val updatedUser = User(
        userId = user.userId,
        name = name,
        email = email,
        password = user.password,
        points = user.points,
        profileImageUrl = user.profileImageUrl,
        location = user.location,
    )
    usersViewsModels.updateUser(updatedUser)
    onUpdateOutcome(true)
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
               // ProfileEditingScreen(navController)
            }
        }
    }
}