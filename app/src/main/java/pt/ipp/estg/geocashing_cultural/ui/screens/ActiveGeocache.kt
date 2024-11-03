package pt.ipp.estg.geocashing_cultural.ui.screens

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import pt.ipp.estg.geocashing_cultural.R
import pt.ipp.estg.geocashing_cultural.ui.theme.Black
import pt.ipp.estg.geocashing_cultural.ui.theme.LightGray
import pt.ipp.estg.geocashing_cultural.ui.theme.Purple
import pt.ipp.estg.geocashing_cultural.ui.theme.White
import pt.ipp.estg.geocashing_cultural.ui.utils.MyTextButton
import pt.ipp.estg.geocashing_cultural.ui.utils.VerticalSpacer


@Composable
fun ActiveGeocacheScreen(navController: NavHostController) {
    var walking by remember { mutableStateOf(true) }

    Column(modifier = Modifier.padding(28.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column() {
                IconButton({}) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "open Map",
                        tint = Purple,
                        modifier = Modifier.size(56.dp)
                    )
                }
                Text(text = "Localização", color = LightGray)
            }

            Column() {
                IconButton({}) {
                    Image(
                        painter = painterResource(R.drawable.gastronomia),
                        contentDescription = "Categoria Selecionada",
                        modifier = Modifier.size(56.dp)
                    )
                }
                Text(text = "Categoria\nSelecionada", color = LightGray)
            }

            Column() {
                IconButton({}) {
                    Text(text = "?", fontSize = 44.sp, color = Purple)
                }
                Text(text = "Dica", color = LightGray)
            }
        }
        MyTextButton(
            text = if (walking) "Stop Walking" else "Start Walking",
            onClick = { walking = !walking }
        )

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.75f)
        ) {
            // Update 'walking' based on activity recognition results (e.g., from a view model)
            AnimatedDrawable(walking = walking)
            ProgressBar(3)

        }

        ShowTip(
            1,
            "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas\""
        )
    }
}

@Composable
fun AnimatedDrawable(walking: Boolean) {
    var isAnimating by remember { mutableStateOf(false) }

    // Observe changes to `walking` and update `isAnimating`
    LaunchedEffect(walking) {
        isAnimating = walking
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        factory = { context ->
            val imageView = ImageView(context).apply {
                val animation = ContextCompat.getDrawable(
                    context,
                    R.drawable.walking_animation
                ) as AnimationDrawable
                setImageDrawable(animation)
                animation.isOneShot = false
                if (isAnimating) {
                    animation.start()
                } else {
                    animation.stop()
                }
            }
            imageView
        },
        update = { imageView ->
            val animation = imageView.drawable as AnimationDrawable
            if (isAnimating) {
                animation.start()
            } else {
                animation.stop()
            }
        }
    )
}

@Composable
fun ProgressBar(tipNumber: Number) {
    val progression = remember { mutableFloatStateOf(tipNumber.toFloat() / 4) }

    Column()
    {
        Text(
            text = "$tipNumber de 4",
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            Modifier
                .height(28.dp)
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(10.dp))
                .padding(4.dp)
        ) {
            Spacer(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progression.floatValue)
                    .background(color = Black, shape = RoundedCornerShape(10.dp))
                    .zIndex(1000f)
            )
        }
    }
}

@Composable
fun ShowTip(tipNumber: Number, tip: String) {
    VerticalSpacer()
    Text("Dica $tipNumber", color = LightGray, fontSize = 14.sp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(10.dp), color = White)
            .padding(10.dp)
    ) {
        Text(tip, fontSize = 14.sp, lineHeight = 14.sp)
    }
}