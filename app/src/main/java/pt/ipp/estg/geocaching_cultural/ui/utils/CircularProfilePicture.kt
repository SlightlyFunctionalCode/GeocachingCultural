package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.White

@Composable
fun CircularProfilePicture(
    profileImageUrl: String?,
    profileImageDefault: Int?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = White, shape = CircleShape
            )
            .clip(CircleShape)
    ) {
        if (profileImageUrl == "" || profileImageUrl == null) {
            profileImageDefault?.let { painterResource(id = it) }?.let {
                Image(
                    painter = it,
                    contentDescription = stringResource(id = R.string.avatar_description)
                )
            }
        } else {
            GlideImage(
                imageModel = { profileImageUrl }, // loading a network image using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
            )
        }
    }
}