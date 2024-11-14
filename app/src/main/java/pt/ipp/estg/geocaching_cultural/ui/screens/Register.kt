package pt.ipp.estg.geocaching_cultural.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.LargeVerticalSpacer
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocaching_cultural.ui.utils.MyTextField
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer

@Composable
fun RegisterScreen(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    parameter: String?
) {
    var answerName by remember { mutableStateOf("") }
    var answerEmail by remember { mutableStateOf(parameter ?: "") }
    var answerPassword by remember { mutableStateOf("") }
    var answerConfirmPassword by remember { mutableStateOf("") }

    var buttonState by remember { mutableStateOf(false) }

    var isEmailValid by remember { mutableStateOf(true) }
    var isNameValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isConfirmPasswordValid by remember { mutableStateOf(true) }

    var supportingTextName by remember { mutableStateOf("") }
    var supportingTextEmail by remember { mutableStateOf("") }
    var supportingTextPassword by remember { mutableStateOf("") }
    var supportingConfirmTextPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        var registrationSuccessful by remember { mutableStateOf(false) }

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
                    text = stringResource(R.string.register),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            }

            item {
                VerticalSpacer()

                MyTextField(
                    label = { Text(stringResource(R.string.name_label)) },
                    value = answerName,
                    onValueChange = {
                        answerName = it
                        isNameValid = it.isNotBlank()
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = stringResource(R.string.name_icon)
                        )
                    },
                    isError = !isNameValid,
                    supportingText = { Text(text = supportingTextName, color = Pink) },
                    modifier = Modifier
                )
                supportingTextName =
                    if (!isNameValid) stringResource(R.string.name_error_message) else ""
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

            item {
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

            item {
                VerticalSpacer()
                var showPassword by remember { mutableStateOf(false) }

                MyTextField(
                    label = { Text(stringResource(R.string.password_confirmation_label)) },
                    value = answerConfirmPassword,
                    onValueChange = {
                        answerConfirmPassword = it
                        isConfirmPasswordValid =
                            it.length >= 6  // Password must be at least 6 characters
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
                    isError = !isConfirmPasswordValid,
                    supportingText = { Text(text = supportingConfirmTextPassword, color = Pink) },
                    modifier = Modifier
                )
                supportingConfirmTextPassword =
                    if (!isConfirmPasswordValid) {
                        stringResource(R.string.password_error_message)
                    } else if (answerPassword != answerConfirmPassword) {
                        stringResource(R.string.different_password)
                    } else {
                        ""
                    }
            }

            /* TODO: add firebase autentication */
            item {
                VerticalSpacer()
                Row {
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = stringResource(R.string.open_login_icons),
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = stringResource(R.string.open_login_icons),
                            modifier = Modifier.size(54.dp)
                        )
                    }
                    IconButton({}) {
                        Image(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = stringResource(R.string.open_login_icons),
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }
            }

            buttonState = isNameValid &&
                    isPasswordValid &&
                    isEmailValid &&
                    isConfirmPasswordValid &&
                    answerName != "" &&
                    answerEmail != "" &&
                    answerPassword != "" &&
                    answerConfirmPassword != ""

            item {
                VerticalSpacer()
                MyTextButton(
                    text = stringResource(R.string.submit),
                    enabled = buttonState && !registrationSuccessful,
                    onClick = {
                        register(
                            answerName,
                            answerEmail,
                            answerPassword,
                            usersViewsModels,
                            onRegistrationOutcome = { outcome ->
                                if (outcome) {
                                    registrationSuccessful = true
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                LaunchedEffect(registrationSuccessful) {
                    if (registrationSuccessful) {
                        snackbarHostState.showSnackbar(context.getString(R.string.successful_register))
                        navController.navigate("loginScreen")
                        registrationSuccessful = false
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

fun register(
    name: String,
    email: String,
    password: String,
    usersViewsModels: UsersViewsModels,
    onRegistrationOutcome: (Boolean) -> Unit
) {
    val user = User(
        userId = 0,
        name = name,
        email = email,
        password = password,
        points = 0,
        profileImageUrl = null,
        location = Location(0.0, 0.0),
        profilePictureDefault = R.drawable.avatar_male_01
    )
    usersViewsModels.insertUser(user)
    onRegistrationOutcome(true)
}


@Preview
@Composable
fun RegisterPreview() {
    val navController = rememberNavController()

    Geocaching_CulturalTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            var answerName by remember { mutableStateOf("") }
            var answerEmail by remember { mutableStateOf(  "") }
            var answerPassword by remember { mutableStateOf("") }
            var answerConfirmPassword by remember { mutableStateOf("") }

            var buttonState by remember { mutableStateOf(false) }

            var isEmailValid by remember { mutableStateOf(true) }
            var isNameValid by remember { mutableStateOf(true) }
            var isPasswordValid by remember { mutableStateOf(true) }
            var isConfirmPasswordValid by remember { mutableStateOf(true) }

            var supportingTextName by remember { mutableStateOf("") }
            var supportingTextEmail by remember { mutableStateOf("") }
            var supportingTextPassword by remember { mutableStateOf("") }
            var supportingConfirmTextPassword by remember { mutableStateOf("") }

            Box(modifier = Modifier.fillMaxSize()) {
                val registrationSuccessful by remember { mutableStateOf(false) }

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
                            text = stringResource(R.string.register),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    item {
                        VerticalSpacer()

                        MyTextField(
                            label = { Text(stringResource(R.string.name_label)) },
                            value = answerName,
                            onValueChange = {
                                answerName = it
                                isNameValid = it.isNotBlank()
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Person,
                                    contentDescription = stringResource(R.string.name_icon)
                                )
                            },
                            isError = !isNameValid,
                            supportingText = { Text(text = supportingTextName, color = Pink) },
                            modifier = Modifier
                        )
                        supportingTextName =
                            if (!isNameValid) stringResource(R.string.name_error_message) else ""
                    }

                    item {
                        VerticalSpacer()

                        MyTextField(
                            label = { Text(stringResource(R.string.email_label)) },
                            value = answerEmail,
                            onValueChange = {
                                answerEmail = it
                                isEmailValid =
                                    android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
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

                    item {
                        VerticalSpacer()
                        var showPassword by remember { mutableStateOf(false) }

                        MyTextField(
                            label = { Text(stringResource(R.string.password_label)) },
                            value = answerPassword,
                            onValueChange = {
                                answerPassword = it
                                isPasswordValid =
                                    it.length >= 6  // Password must be at least 6 characters
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

                    item {
                        VerticalSpacer()
                        var showPassword by remember { mutableStateOf(false) }

                        MyTextField(
                            label = { Text(stringResource(R.string.password_confirmation_label)) },
                            value = answerConfirmPassword,
                            onValueChange = {
                                answerConfirmPassword = it
                                isConfirmPasswordValid =
                                    it.length >= 6  // Password must be at least 6 characters
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
                            isError = !isConfirmPasswordValid,
                            supportingText = {
                                Text(
                                    text = supportingConfirmTextPassword,
                                    color = Pink
                                )
                            },
                            modifier = Modifier
                        )
                        supportingConfirmTextPassword =
                            if (!isConfirmPasswordValid) {
                                stringResource(R.string.password_error_message)
                            } else if (answerPassword != answerConfirmPassword) {
                                stringResource(R.string.different_password)
                            } else {
                                ""
                            }
                    }

                    /* TODO: add firebase autentication */
                    item {
                        VerticalSpacer()
                        Row {
                            IconButton({}) {
                                Image(
                                    painter = painterResource(id = R.drawable.facebook),
                                    contentDescription = stringResource(R.string.open_login_icons),
                                    modifier = Modifier.size(54.dp)
                                )
                            }
                            IconButton({}) {
                                Image(
                                    painter = painterResource(id = R.drawable.facebook),
                                    contentDescription = stringResource(R.string.open_login_icons),
                                    modifier = Modifier.size(54.dp)
                                )
                            }
                            IconButton({}) {
                                Image(
                                    painter = painterResource(id = R.drawable.facebook),
                                    contentDescription = stringResource(R.string.open_login_icons),
                                    modifier = Modifier.size(54.dp)
                                )
                            }
                        }
                    }

                    buttonState = isNameValid &&
                            isPasswordValid &&
                            isEmailValid &&
                            isConfirmPasswordValid &&
                            answerName != "" &&
                            answerEmail != "" &&
                            answerPassword != "" &&
                            answerConfirmPassword != ""

                    item {
                        VerticalSpacer()
                        MyTextButton(
                            text = stringResource(R.string.submit),
                            enabled = buttonState && !registrationSuccessful,
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        )

                        LargeVerticalSpacer()
                    }
                }
            }
        }
    }
}