package pt.ipp.estg.geocaching_cultural.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Black
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.Pink
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
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

    var supportingTextProfilePicUrlValid by remember { mutableStateOf("") }

    var updateSuccessful by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(28.dp)) {
        Title(text = stringResource(R.string.change_profile_pic))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.current_profile_pic))

            ProfilePicture(
                currentUser.value?.profileImageUrl,
                currentUser.value?.profilePictureDefault
            )

            VerticalSpacer()

            MyTextButton(
                text = stringResource(R.string.remove_custom_profile_pic),
                { answerProfilePicUrl = "" })

            VerticalSpacer()

            Column {
                MyTextField(
                    label = { Text(stringResource(R.string.choose_custom_profile_pic)) },
                    value = answerProfilePicUrl,
                    onValueChange = {
                        answerProfilePicUrl = it
                        isProfilePicUrlValid = android.util.Patterns.WEB_URL.matcher(it).matches()
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Face,
                            contentDescription = stringResource(R.string.profile_pic_icon)
                        )
                    },
                    isError = !isProfilePicUrlValid,
                    supportingText = {
                        Text(
                            text = supportingTextProfilePicUrlValid,
                            color = Pink
                        )
                    },
                    modifier = Modifier
                )

                supportingTextProfilePicUrlValid =
                    if (!isProfilePicUrlValid) stringResource(R.string.url_error_message) else ""

                VerticalSpacer()

                FlowRow(
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (image in defaultProfilePics)
                        Image(
                            painter = painterResource(image),
                            contentDescription = stringResource(R.string.default_avatar_pic),
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
                                        Modifier
                                    }
                                )
                        )
                }
            }

            buttonState = isProfilePicUrlValid

            VerticalSpacer()
            MyTextButton(
                text = stringResource(R.string.submit),
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
                    Toast.makeText(
                        context,
                        context.getString(R.string.profile_updated_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("profileScreen")
                    updateSuccessful = false
                }
            }
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

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
fun ChooseProfilePicScreenPreview() {
    val defaultProfilePics = listOf(
        R.drawable.avatar_male_01,
        R.drawable.avatar_male_02,
        R.drawable.avatar_male_03,
        R.drawable.avatar_female_01,
        R.drawable.avatar_female_02,
        R.drawable.avatar_female_03
    )

    var answerProfilePicUrl = ""
    var answerProfilePicDefault = R.drawable.avatar_male_01
    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                Column(modifier = Modifier.padding(28.dp)) {
                    Title(text = stringResource(R.string.change_profile_pic))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.current_profile_pic))

                        ProfilePicture(
                            "",
                            R.drawable.avatar_male_01
                        )

                        VerticalSpacer()

                        MyTextButton(
                            text = stringResource(R.string.remove_custom_profile_pic),
                            { answerProfilePicUrl = "" })

                        VerticalSpacer()

                        Column {
                            MyTextField(
                                label = { Text(stringResource(R.string.choose_custom_profile_pic)) },
                                value = answerProfilePicUrl,
                                onValueChange = { },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Face,
                                        contentDescription = stringResource(R.string.profile_pic_icon)
                                    )
                                },
                                modifier = Modifier
                            )

                            VerticalSpacer()

                            FlowRow(
                                verticalArrangement = Arrangement.Center,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                for (image in defaultProfilePics)
                                    Image(
                                        painter = painterResource(image),
                                        contentDescription = stringResource(R.string.default_avatar_pic),
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
                                                    Modifier
                                                }
                                            )
                                    )
                            }
                        }

                        VerticalSpacer()
                        MyTextButton(
                            text = stringResource(R.string.submit),
                            onClick = {},
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

