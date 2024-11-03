package pt.ipp.estg.geocashing_cultural.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Geocashing_CulturalTheme
import pt.ipp.estg.geocashing_cultural.ui.theme.Yellow

@Composable
fun ScoreboardScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row {
            Icon(
                painter = painterResource(R.drawable.trofeu), // Substitua pelo seu ícone de troféu
                contentDescription = "Ícone",
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = "ScoreBoard",
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Exibe o Top 10 com o Top 3 dentro
        Top10Players(
            listOf(
                "Alice" to 1500,
                "Bob" to 1400,
                "Charlie" to 1300,
                "Dave" to 1200,
                "Eve" to 1100,
                "Frank" to 1000,
                "Grace" to 900,
                "Heidi" to 800,
                "Ivan" to 700,
                "Judy" to 600
            )
        )
    }
}

@Composable
fun Top10Players(players: List<Pair<String, Int>>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Exibe o Top 3 com ícones adicionais
        Top3Players(players.take(3))

        // Exibe o restante dos jogadores, começando do 4º lugar
        players.drop(3).take(7).forEachIndexed { index, (playerName, points) ->
            Player(
                playerName = playerName,
                points = points,
                rank = index + 4
            ) // Começa a contagem a partir do 4º lugar
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun Top3Players(players: List<Pair<String, Int>>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        players.take(3).forEachIndexed { index, (playerName, points) ->
            val icon = when (index) {
                0 -> painterResource(R.drawable.taca_de_ouro)    // Ícone do 1º lugar
                1 -> painterResource(R.drawable.taca_de_prata)   // Ícone do 2º lugar
                2 -> painterResource(R.drawable.taca_de_bronze)   // Ícone do 3º lugar
                else -> null
            }

            PlayerWithIcon(playerName = playerName, points = points, rank = index + 1, icon = icon)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Player(playerName: String, points: Int, rank: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$rank. $playerName - $points pontos", fontSize = 22.sp)
    }
}

@Composable
fun PlayerWithIcon(playerName: String, points: Int, rank: Int, icon: Painter?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$rank. $playerName - $points pontos", fontSize = 22.sp)

        // Exibe o ícone adicional, se disponível
        icon?.let {
            Image(
                painter = it,
                contentDescription = "Ícone posição $rank",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun ScoreboardPreview() {
    val navController = rememberNavController()
    Geocashing_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                ScoreboardScreen(navController)
            }
        }
    }
}
