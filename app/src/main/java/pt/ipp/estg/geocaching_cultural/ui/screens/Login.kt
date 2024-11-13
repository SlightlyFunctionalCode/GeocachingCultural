package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.LargeVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun LoginScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    var answerEmail by remember { mutableStateOf("") }
    var answerPassword by remember { mutableStateOf("") }

    var buttonState by remember { mutableStateOf(false) }

    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    var supportingTextEmail by remember { mutableStateOf("") }
    var supportingTextPassword by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    var loginError by remember { mutableStateOf(false) }
    var loginSuccessful by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (usersViewsModels.currentUserId.value != -1) {
        navController.navigate("homeScreen")
    }

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
                    text = stringResource(R.string.login),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }

            item {
                VerticalSpacer()
                MyTextField(
                    label = { Text(stringResource(R.string.email_label)) },
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
            }

            item { /*TODO: Fazer ser possível ver a password */
                VerticalSpacer()
                var showPassword by remember { mutableStateOf(false) }
                MyTextField(
                    label = { Text(stringResource(R.string.password_label)) },
                    value = answerPassword,
                    onValueChange = {
                        answerPassword = it
                        isPasswordValid = it.length >= 6  // Password must be at least 6 characters
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) ImageVector.vectorResource(R.drawable.visibility) else ImageVector.vectorResource(
                                    R.drawable.visibility_off
                                ),
                                contentDescription = if (showPassword) stringResource(R.string.hide_password) else stringResource(
                                    R.string.show_password
                                )
                            )
                        }
                    },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    isError = !isPasswordValid,
                    supportingText = { Text(text = supportingTextPassword, color = Pink) },
                    modifier = Modifier
                )
                supportingTextPassword =
                    if (!isPasswordValid) stringResource(R.string.password_error_message) else ""
            }

            buttonState = isPasswordValid &&
                    isEmailValid &&
                    answerEmail != "" &&
                    answerPassword != ""

            item {
                VerticalSpacer()
                MyTextButton(
                    text = stringResource(R.string.submit),
                    enabled = buttonState,
                    onClick = {
                        login(
                            answerEmail,
                            answerPassword,
                            usersViewsModels,
                            onLoginOutcome = { outcome ->
                                if (!outcome) loginError = true else loginSuccessful = true
                            },
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                LaunchedEffect(loginError) {
                    if (loginError) {
                        snackbarHostState.showSnackbar(context.getString(R.string.invalid_credentials_login))
                        loginError = false
                    }
                }

                LaunchedEffect(loginSuccessful) {
                    if (loginSuccessful) {
                        navController.navigate("homeScreen")
                    }
                }

                SnackbarHost(
                    hostState = snackbarHostState,
                )

                LargeVerticalSpacer()
            }
        }
    }
}

fun login(
    email: String,
    password: String,
    usersViewsModels: UsersViewsModels,
    onLoginOutcome: (Boolean) -> Unit,
) {
    usersViewsModels.getUserWithLogin(email, password) { user ->
        if (user != null) {
            onLoginOutcome(true)
            usersViewsModels.saveCurrentUserId(user.userId)
        } else {
            onLoginOutcome(false)
        }
    }
}


@Preview
@Composable
fun LoginPreview() {
    val navController = rememberNavController()

    Geocaching_CulturalTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            //  LoginScreen(navController)
        }
    }
}
