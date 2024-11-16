package pt.ipp.estg.geocaching_cultural.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import pt.ipp.estg.geocaching_cultural.R
import pt.ipp.estg.geocaching_cultural.database.classes.Geocache
import pt.ipp.estg.geocaching_cultural.database.classes.Hint
import pt.ipp.estg.geocaching_cultural.database.classes.Location
import pt.ipp.estg.geocaching_cultural.database.classes.User
import pt.ipp.estg.geocaching_cultural.database.classes.enums.GeocacheType
import pt.ipp.estg.geocaching_cultural.database.viewModels.GeocacheViewsModels
import pt.ipp.estg.geocaching_cultural.database.viewModels.UsersViewsModels
import pt.ipp.estg.geocaching_cultural.ui.theme.Black
import pt.ipp.estg.geocaching_cultural.ui.theme.Geocaching_CulturalTheme
import pt.ipp.estg.geocaching_cultural.ui.theme.LightGray
import pt.ipp.estg.geocaching_cultural.ui.theme.Purple
import pt.ipp.estg.geocaching_cultural.ui.theme.White
import pt.ipp.estg.geocaching_cultural.ui.theme.Yellow
import pt.ipp.estg.geocaching_cultural.ui.utils.VerticalSpacer
import java.time.LocalDateTime

@Composable
fun ActiveGeocacheScreen(
    navController: NavHostController,
    usersViewsModels: UsersViewsModels,
    geocacheViewsModels: GeocacheViewsModels
) {
    val context = LocalContext.current
    // Start location updates when the screen is opened
    LaunchedEffect(Unit) {
        usersViewsModels.startLocationUpdates(context)
        usersViewsModels.startSensorUpdates(context)
    }

    // Stop location updates when the screen is exited (using DisposableEffect or other lifecycle management methods)
    DisposableEffect(Unit) {
        onDispose {
            usersViewsModels.stopLocationUpdates()
            usersViewsModels.stopSensorUpdates()
        }
    }

    val geocache =
        Geocache(0, Location(0.0, 0.0), GeocacheType.CULTURAL, "", "", null, LocalDateTime.now(), 0)

    // AnswerQuestionDialog()

    val currentUser = usersViewsModels.currentUser.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(28.dp)

    ) {
        Column {
            IconSection(context, currentUser.value, geocache, navController)

            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.75f)
            ) {
                VerticalSpacer()
                AnimatedDrawable(isWalking = currentUser.value?.isWalking ?: false)
                ProgressBar(3)
                Text(text = " Location: ${currentUser.value!!.location.lat},${currentUser.value!!.location.lng}")
            }

            ShowHint(
                1,
                Hint(
                    0,
                    0,
                    "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas\""
                )
            )
        }
    }
}

@Composable
fun IconSection(
    context: Context,
    currentUser: User?,
    geocache: Geocache,
    navController: NavHostController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (currentUser != null) {
            IconButton({
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:${currentUser.location.lat},${currentUser.location.lng}")
                )
                startActivity(context, intent, null)
            }) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = stringResource(R.string.open_map_icon),
                    tint = Purple,
                    modifier = Modifier
                        .size(56.dp)
                )
            }
        }

        IconButton({
            navController.navigate("explorarScreen/${geocache.type}")
        }) {
            Image(
                painter = when (geocache.type) {
                    GeocacheType.GASTRONOMIA -> painterResource(R.drawable.gastronomia)
                    GeocacheType.CULTURAL -> painterResource(R.drawable.cultural)
                    GeocacheType.HISTORICO -> painterResource(R.drawable.historico)
                },
                contentDescription = stringResource(R.string.category_selected),
                modifier = Modifier
                    .size(56.dp)
            )
        }

        /*TODO abrir uma dica random */
        IconButton({}) {
            Text(text = "?", fontSize = 44.sp, color = Purple)
        }
    }
}

@Composable
fun AnimatedDrawable(isWalking: Boolean) {

    var animationState by remember { mutableStateOf("sitting_down") }

    // Observe changes to `isWalking` and update `animationState`
    LaunchedEffect(isWalking) {
        animationState = when {
            isWalking && animationState == "sitting_down" -> "getting_up"
            isWalking && animationState == "walking" -> "walking"
            !isWalking && animationState == "walking" -> "sitting_down"
            !isWalking && animationState == "getting_up" -> "sitting_down"
            else -> animationState
        }
    }

    AndroidView(
        modifier = Modifier.size(250.dp),
        factory = { context ->
            val imageView = ImageView(context)
            updateAnimationDrawable(context, imageView, animationState) {
                // Triggered after `getting_up` completes
                if (animationState == "getting_up" && isWalking) {
                    animationState = "walking"
                }
            }
            imageView
        },
        update = { imageView ->
            updateAnimationDrawable(imageView.context, imageView, animationState) {
                if (animationState == "getting_up" && isWalking) {
                    animationState = "walking"
                }
            }
        }
    )
}

private fun updateAnimationDrawable(
    context: Context,
    imageView: ImageView,
    animationState: String,
    onAnimationEnd: (() -> Unit)? = null
) {
    val animationDrawable = when (animationState) {
        "walking" -> ContextCompat.getDrawable(
            context,
            R.drawable.walking_animation
        ) as AnimationDrawable

        "getting_up" -> ContextCompat.getDrawable(
            context,
            R.drawable.getting_up_animation
        ) as AnimationDrawable

        "sitting_down" -> ContextCompat.getDrawable(
            context,
            R.drawable.sitting_down_animation
        ) as AnimationDrawable

        else -> null
    }

    animationDrawable?.let { drawable ->
        imageView.setImageDrawable(drawable)
        drawable.isOneShot = animationState != "walking" // Loop `walking`, not others

        // Calculate total animation duration for `getting_up` to use a delay
        if (animationState == "getting_up") {
            val totalDuration = (0 until drawable.numberOfFrames).sumOf { drawable.getDuration(it) }

            // Start the animation and set a delay to trigger the callback after it completes
            drawable.start()
            Handler(Looper.getMainLooper()).postDelayed({
                onAnimationEnd?.invoke()
            }, totalDuration.toLong())
        } else {
            // Directly start for other states
            drawable.start()
        }
    }
}

@Composable
fun ProgressBar(questionNumber: Number) {
    val progression = remember { mutableFloatStateOf(questionNumber.toFloat() / 4) }

    Column()
    {
        Text(
            text = "$questionNumber / 4",
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
fun ShowHint(hintNumber: Number, hint: Hint) {
    Column {
        VerticalSpacer()
        Text("${stringResource(R.string.hint)} $hintNumber", color = LightGray, fontSize = 14.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(shape = RoundedCornerShape(10.dp), color = White)
                .padding(10.dp)
        ) {
            Text(hint.hint, fontSize = 14.sp, lineHeight = 14.sp)
        }
    }
}

@Preview
@Composable
fun ActiveGeocachePreview() {
    val navController = rememberNavController()

    val user = User(
        1,
        name = "admin",
        email = "admin@ad.ad",
        password = "admin123",
        points = 100,
        profileImageUrl = "avatar",
        profilePictureDefault = 1,
        isWalking = false,
        location = Location(0.0, 0.0),
    )

    Geocaching_CulturalTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Yellow)
        ) {
            item {
                val context = LocalContext.current

                val geocache =
                    Geocache(
                        0,
                        Location(0.0, 0.0),
                        GeocacheType.CULTURAL,
                        "",
                        "",
                        null,
                        LocalDateTime.now(),
                        0
                    )

                Box(
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .padding(28.dp)

                ) {
                    Column {
                        IconSection(context, user, geocache, navController)

                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.75f)
                        ) {
                            VerticalSpacer()
                            Image(
                                painter = painterResource(id = R.drawable.walking_animation_01),
                                contentDescription = "Avatar"
                            )
                            Column()
                            {
                                Text(
                                    text = "3 / 4",
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Box(
                                    Modifier
                                        .height(28.dp)
                                        .fillMaxWidth()
                                        .background(
                                            color = White,
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .padding(4.dp)
                                ) {
                                    Spacer(
                                        Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(3 / 4.toFloat())
                                            .background(
                                                color = Black,
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .zIndex(1000f)
                                    )
                                }
                            }
                            Text(text = " Location: ${user.location.lat},${user.location.lng}")
                        }

                        ShowHint(
                            1,
                            Hint(
                                0,
                                0,
                                "\"Um local onde pode encontrar tudo para encher o carrinho, desde produtos frescos até marcas exclusivas\""
                            )
                        )
                    }
                }
            }
        }
    }
}