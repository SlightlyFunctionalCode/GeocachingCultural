package pt.ipp.estg.geocashing_cultural.ui.components.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray

@Composable
fun Footer() {
    BottomAppBar(
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "© GeoCultura Explorer Inc 2024",
                    color = LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(2f)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = stringResource(id = R.string.facebook_link)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = stringResource(id = R.string.instagram_link)
                    )

                }
            }
        },
    )
}