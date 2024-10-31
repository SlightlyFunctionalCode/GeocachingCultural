package pt.ipp.estg.geocashing_cultural.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Pink
import pt.ipp.estg.geocashing_cultural.ui.theme.White

@Composable
fun ProfileScreen() {
    LazyColumn(modifier = Modifier.padding(28.dp)) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perfil",
                    fontWeight = FontWeight.Bold,
                    fontSize = 56.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1.25f)
                )

                Spacer(Modifier.width(28.dp))

                Box(
                    Modifier
                        .background(color = White, shape = RoundedCornerShape(10.dp))
                        .height(62.dp)
                        .weight(1f)
                ){
                    Column (horizontalAlignment = Alignment.CenterHorizontally){
                        Text(
                            text = "Points",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1.25f)
                        )
                        Row {
                            Text(
                                text = "62",
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                textAlign = TextAlign.Center,
                                color = Pink,
                                modifier = Modifier.weight(1.25f)
                            )
                            Icon(
                                painter = painterResource(R.drawable.facebook),
                                contentDescription = "Coins"
                            )
                        }
                    }
                }
            }
        }
    }
}