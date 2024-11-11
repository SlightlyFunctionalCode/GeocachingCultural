package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.White

@Composable
fun ProfilePicture(profileImageUrl: String?, profileImageDefault: Int?, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(0.75f)
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(15.dp)
    ) {
        if (profileImageUrl == "" || profileImageUrl == null) {
            profileImageDefault?.let { painterResource(it) }?.let {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = it,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.clip(RoundedCornerShape(5.dp))
                )
            }
        } else {
            GlideImage(
                imageModel = { profileImageUrl }, // loading a network image using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                modifier = Modifier.clip(RoundedCornerShape(5.dp))
            )
        }
    }
}


