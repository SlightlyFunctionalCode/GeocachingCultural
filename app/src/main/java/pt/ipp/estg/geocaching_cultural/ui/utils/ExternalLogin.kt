package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.ipp.estg.geocaching_cultural.R

@Composable
fun ExternalLogin() {
    Row {
        IconButton({}) {
            Image(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = stringResource(R.string.open_login_icons),
                modifier = Modifier.size(54.dp)
            )
        }
        SmallHorizontalSpacer()
        IconButton({}) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = stringResource(R.string.open_login_icons),
                modifier = Modifier.size(54.dp)
            )
        }
        SmallHorizontalSpacer()
        IconButton({}) {
            Image(
                painter = painterResource(id = R.drawable.twitter),
                contentDescription = stringResource(R.string.open_login_icons),
                modifier = Modifier.size(54.dp)
            )
        }
    }
}