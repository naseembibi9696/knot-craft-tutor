package com.knotcraft.tutor.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartDeep
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.ui.AccentRule
import com.knotcraft.tutor.core.ui.ScreenBackground
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(
        key = "splash",
        factory = AppViewModelFactory()
    )
    val state by splashViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.finished) {
        if (state.finished) {
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    var appeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        appeared = true
    }

    val transition = rememberInfiniteTransition(label = "splashLoops")
    val halo by transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )
    val haloAlpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "haloAlpha"
    )
    val roseAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rose"
    )
    val dashPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dash"
    )

    val logoScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.82f,
        animationSpec = tween(520, easing = FastOutSlowInEasing),
        label = "logoScale"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(620, delayMillis = 180),
        label = "titleAlpha"
    )

    ScreenBackground(
        overlay = listOf(
            ChartDeep.copy(alpha = 0.92f),
            ChartMid.copy(alpha = 0.94f),
            ChartBase.copy(alpha = 0.97f)
        ),
        imageAlpha = 0.28f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(260.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize().alpha(0.5f)) {
                    rotate(roseAngle) {
                        val radius = size.minDimension / 2f
                        repeat(16) { index ->
                            val angle = (Math.PI * 2.0 * index / 16.0).toFloat()
                            drawLine(
                                color = ChipNavy,
                                start = center + Offset(
                                    cos(angle) * radius * 0.28f,
                                    sin(angle) * radius * 0.28f
                                ),
                                end = center + Offset(
                                    cos(angle) * radius,
                                    sin(angle) * radius
                                ),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(halo)
                        .alpha(haloAlpha)
                        .background(
                            Brush.radialGradient(
                                listOf(RopeGold.copy(alpha = 0.45f), RopeGold.copy(alpha = 0f))
                            )
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.desc_emblem),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(180.dp)
                        .scale(logoScale)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Canvas(
                modifier = Modifier
                    .width(220.dp)
                    .height(42.dp)
            ) {
                val path = androidx.compose.ui.graphics.Path()
                path.moveTo(0f, size.height * 0.7f)
                path.cubicTo(
                    size.width * 0.28f, size.height * -0.25f,
                    size.width * 0.72f, size.height * 1.25f,
                    size.width, size.height * 0.3f
                )
                drawPath(
                    path = path,
                    color = RopeGold.copy(alpha = 0.75f),
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(14f, 10f),
                            dashPhase
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.brand_line_one),
                style = MaterialTheme.typography.displaySmall,
                color = Sailcloth,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )
            Text(
                text = stringResource(R.string.brand_line_two),
                style = MaterialTheme.typography.displaySmall,
                color = Sailcloth,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccentRule(width = 64, accent = RopeGold)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                letterSpacing = 4.sp,
                color = MistBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                color = RopeGold,
                trackColor = ChipNavy,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .background(ChipNavy, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.splash_caption),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = DeepMist,
                textAlign = TextAlign.Center
            )
        }
    }
}
