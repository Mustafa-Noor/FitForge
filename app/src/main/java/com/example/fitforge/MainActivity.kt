package com.example.fitforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.delay
import com.example.fitforge.ui.theme.FitForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitForgeTheme {
                FitForgeApp()
            }
        }
    }
}

@Composable
fun FitForgeApp() {
    var showWelcomeScreen by remember { mutableStateOf(false) }
    var startSplashExit by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2300)
        startSplashExit = true
        delay(700)
        showWelcomeScreen = true
    }

    AnimatedContent(
        targetState = showWelcomeScreen,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
        },
        label = "launchTransition"
    ) { isWelcomeVisible ->
        if (isWelcomeVisible) {
            WelcomeScreen()
        } else {
            SplashScreen(startExit = startSplashExit)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    FitForgeTheme {
        SplashScreen(startExit = false)
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    FitForgeTheme {
        WelcomeScreen()
    }
}

@Composable
fun SplashScreen(startExit: Boolean) {
    var showSplashContent by remember { mutableStateOf(false) }

    val splashAlpha by animateFloatAsState(
        targetValue = if (startExit) 0f else 1f,
        animationSpec = tween(durationMillis = 650),
        label = "splashAlpha"
    )
    val splashScale by animateFloatAsState(
        targetValue = if (startExit) 0.94f else 1f,
        animationSpec = tween(durationMillis = 650),
        label = "splashScale"
    )

    val logoPulse by rememberInfiniteTransition(label = "logoPulse").animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoPulseAnimation"
    )

    val dumbbellOrbitAngle by rememberInfiniteTransition(label = "dumbbellOrbit").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dumbbellOrbitAnimation"
    )

    LaunchedEffect(Unit) {
        showSplashContent = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = showSplashContent && !startExit,
            enter = fadeIn(animationSpec = tween(650)) +
                slideInVertically(initialOffsetY = { it / 2 }) +
                scaleIn(initialScale = 0.92f),
            exit = fadeOut(animationSpec = tween(350)) +
                slideOutVertically(targetOffsetY = { -it / 4 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .rotate(dumbbellOrbitAngle),
                        contentAlignment = Alignment.Center
                    ) {
                        SatelliteDumbbell(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .size(24.dp)
                        )
                        SatelliteDumbbell(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .size(24.dp)
                        )
                    }

                    Image(
                        painter = painterResource(id = R.drawable.ff_gym_logo),
                        contentDescription = stringResource(id = R.string.brand_name),
                        modifier = Modifier
                            .size(180.dp)
                            .scale(logoPulse * splashScale)
                            .graphicsLayer(alpha = splashAlpha)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.brand_name),
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun SatelliteDumbbell(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF1F2937))
            .border(width = 1.dp, brush = SolidColor(Color.White), shape = CircleShape)
    )
}

@Composable
fun WelcomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_title),
            fontSize = 34.sp
        )
    }
}