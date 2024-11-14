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
import androidx.compose.ui.res.stringResource
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
                Title(text = stringResource(R.string.profile), modifier = Modifier.weight(1.25f))
                HorizontalSpacer()
                PointsDisplay(points = user.points, modifier = Modifier.weight(1f))
            }

            VerticalSpacer()
            ProfilePicture(user.profileImageUrl, user.profilePictureDefault)


            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                Text(text = user.name, fontSize = 24.sp)

                Text(text = user.email, color = LightGray)
                SmallVerticalSpacer()
                MyTextButton(text = stringResource(R.string.update_profile_info),
                    { navController.navigate("profileEditingScreen") })
            }


            Column(Modifier.fillMaxWidth()) {
                VerticalSpacer()
                Text(text = stringResource(R.string.extra_actions), fontSize = 24.sp, color = Pink)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MyTextButton(
                        text = stringResource(R.string.delete_profile),
                        onClick = {
                            usersViewsModels.deleteUser(user)
                            usersViewsModels.saveCurrentUserId(-1)
                            navController.navigate("aboutUsScreen")
                        }
                    )
                    SmallHorizontalSpacer()
                    Text(
                        text = stringResource(R.string.irreversible_action),
                        color = Pink,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    val navController = rememberNavController()

    val userState = User(
        1,
        name = "admin",
        email = "admin@ad.ad",
        password = "admin123",
        points = 100,
        profileImageUrl = "",
        profilePictureDefault = R.drawable.avatar_male_01,
        isWalking = true,
        location = Location(0.0, 0.0),
    )

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                userState.let { user ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(28.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Title(text = stringResource(R.string.profile), modifier = Modifier.weight(1.25f))
                            HorizontalSpacer()
                            PointsDisplay(points = user.points, modifier = Modifier.weight(1f))
                        }

                        VerticalSpacer()
                        ProfilePicture(user.profileImageUrl, user.profilePictureDefault)


                        Column(Modifier.fillMaxWidth()) {
                            VerticalSpacer()
                            Text(text = user.name, fontSize = 24.sp)

                            Text(text = user.email, color = LightGray)
                            SmallVerticalSpacer()
                            MyTextButton(text = stringResource(R.string.update_profile_info),
                                { navController.navigate("profileEditingScreen") })
                        }


                        Column(Modifier.fillMaxWidth()) {
                            VerticalSpacer()
                            Text(text = stringResource(R.string.extra_actions), fontSize = 24.sp, color = Pink)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MyTextButton(
                                    text = stringResource(R.string.delete_profile),
                                    onClick = {
                                    }
                                )
                                SmallHorizontalSpacer()
                                Text(
                                    text = stringResource(R.string.irreversible_action),
                                    color = Pink,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}