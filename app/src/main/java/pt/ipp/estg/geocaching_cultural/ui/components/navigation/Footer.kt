package pt.ipp.estg.geocaching_cultural.ui.components.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.Blue
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray

@Composable
fun Footer(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    BottomAppBar(
        containerColor = Blue,
        modifier = modifier,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.footer_app_name),
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
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val packageManager = context.packageManager
                                var intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("fb://profile/https://www.facebook.com/www.geocaching.pt/")
                                )

                                if (intent.resolveActivity(packageManager) == null) {
                                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/www.geocaching.pt/"))
                                }

                                startActivity(context, intent, null)
                            },
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = stringResource(id = R.string.facebook_link)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                val packageManager = context.packageManager

                                var intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("instagram://user?username=geocachingportugal")
                                )

                                if (intent.resolveActivity(packageManager) == null) {
                                    intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/geocachingportugal/"))
                                }

                                startActivity(context, intent, null)
                            },
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = R.drawable.instagram),
                        contentDescription = stringResource(id = R.string.instagram_link)
                    )

                }
            }
        },
    )
}