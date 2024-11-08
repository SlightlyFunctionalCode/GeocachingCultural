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
fun ProfilePicture(imageUrl: String?, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize(0.75f)
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(15.dp)
    ) {
        if (imageUrl == null) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier.clip(RoundedCornerShape(5.dp))
            )
        } else {
            GlideImage(
                imageModel = { imageUrl }, // loading a network image using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop, alignment = Alignment.Center
                ),
                modifier = Modifier.clip(RoundedCornerShape(5.dp))
            )
        }
    }
}


