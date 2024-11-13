package pt.ipp.estg.geocaching_cultural.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.DisplayUser
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.CircularProfilePicture
import pt.ipp.estg.geocaching_cultural.ui.utils.SmallHorizontalSpacer

@Composable
fun ScoreboardScreen(navController: NavHostController, usersViewsModels: UsersViewsModels) {
    val users = usersViewsModels.getTop10Users().observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.trofeu),
                contentDescription = stringResource(R.string.trophy_icon),
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = stringResource(R.string.scoreboard),
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        users.value?.let { Top10Players(it) }
    }
}

@Composable
fun Top10Players(players: List<User>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (index in players.indices) {
            val user = players[index]
            if (index < 3) {
                Top3Players(index, user)
            } else {
                Player(
                    profileImageUrl = user.profileImageUrl,
                    profilePicDefault = user.profilePictureDefault,
                    playerName = user.name,
                    points = user.points,
                    rank = index + 1
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun Top3Players(index: Int, user: User) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val icon = when (index) {
            0 -> painterResource(R.drawable.taca_de_ouro)    // Ícone do 1º lugar
            1 -> painterResource(R.drawable.taca_de_prata)   // Ícone do 2º lugar
            2 -> painterResource(R.drawable.taca_de_bronze)   // Ícone do 3º lugar
            else -> null
        }

        PlayerWithIcon(
            profileImageUrl = user.profileImageUrl,
            profilePicDefault = user.profilePictureDefault,
            playerName = user.name,
            points = user.points,
            rank = index + 1,
            icon = icon
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun Player(
    profileImageUrl: String?,
    profilePicDefault: Int?,
    playerName: String,
    points: Int,
    rank: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreboardRowDetails(profileImageUrl, profilePicDefault, rank, playerName, points)
    }
}

@Composable
fun ScoreboardRowDetails(
    profileImageUrl: String?,
    profilePicDefault: Int?,
    rank: Int,
    playerName: String,
    points: Int
) {
    Row {
        Text(text = "$rank.", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        SmallHorizontalSpacer()
        CircularProfilePicture(profileImageUrl, profilePicDefault, Modifier.size(24.dp))
        SmallHorizontalSpacer()
        Text(text = playerName, fontSize = 22.sp)
        Text(text = " - $points p", color = LightGray, fontSize = 18.sp)
    }
}

@Composable
fun PlayerWithIcon(
    profileImageUrl: String?,
    profilePicDefault: Int?,
    playerName: String,
    points: Int,
    rank: Int,
    icon: Painter?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreboardRowDetails(profileImageUrl, profilePicDefault, rank, playerName, points)

        // Exibe o ícone adicional, se disponível
        icon?.let {
            Image(
                painter = it,
                contentDescription = "${stringResource(R.string.open_login_icons)} $rank",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun ScoreboardPreview() {
    val navController = rememberNavController()
    Geocaching_CulturalTheme {
        val usersViewsModels: UsersViewsModels = viewModel()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                ScoreboardScreen(navController, usersViewsModels)
            }
        }
    }
}
