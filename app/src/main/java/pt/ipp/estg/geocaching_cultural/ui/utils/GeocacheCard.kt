package pt.ipp.estg.geocaching_cultural.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.White

@Composable
fun GeocacheCard(
    title: String,
    description: String,
    points: Number? = null,
    image: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.50f)
            .background(shape = RoundedCornerShape(10.dp), color = White)
    ) {
        if (points != null) {
            Text(
                text = "+$points",
                textAlign = TextAlign.Center,
                color = LightGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .zIndex(1000f)
                    .background(shape = RoundedCornerShape(10.dp), color = White)
                    .padding(horizontal = 10.dp)

            )
        }
        Column(
            modifier = modifier
                .fillMaxSize(0.85f)
                .background(shape = RoundedCornerShape(10.dp), color = White)
                .padding(25.dp)
        )  {
                Image(
                    painter = BitmapPainter(image),
                    contentDescription =  stringResource(R.string.geocache_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp))
                )
            Column(Modifier.padding(28.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 28.sp)

                Text(text = description, color = LightGray)
            }
        }
    }
}