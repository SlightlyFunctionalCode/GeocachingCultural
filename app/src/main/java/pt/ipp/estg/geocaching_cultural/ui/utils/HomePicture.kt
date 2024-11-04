package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.White

@Composable
fun HomePicture(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight(0.5f)
            .rotate(-3f)
            .background(
                color = White,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(15.dp)
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = R.drawable.home),
            contentDescription = "Home Picture"
        )
    }
}