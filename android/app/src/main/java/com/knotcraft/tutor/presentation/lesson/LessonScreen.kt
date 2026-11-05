package com.knotcraft.tutor.presentation.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.core.ui.PrimaryButton
import com.knotcraft.tutor.core.ui.RopeCanvas
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.core.ui.SecondaryButton
import com.knotcraft.tutor.presentation.common.ropeColorOf

@Composable
fun LessonScreen(
    knotId: String,
    onBack: () -> Unit,
    onStartTrace: () -> Unit
) {
    val lessonViewModel: LessonViewModel = viewModel(
        key = "lesson_$knotId",
        factory = AppViewModelFactory(knotId = knotId)
    )
    val state by lessonViewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(knotId) {
        onDispose { lessonViewModel.pause() }
    }

    LessonContent(
        state = state,
        onBack = onBack,
        onTogglePlay = lessonViewModel::onTogglePlay,
        onScrub = lessonViewModel::onScrub,
        onScrubFinished = lessonViewModel::onScrubFinished,
        onPreviousStep = lessonViewModel::onPreviousStep,
        onNextStep = lessonViewModel::onNextStep,
        onStartTrace = onStartTrace
    )
}

@Composable
private fun LessonContent(
    state: LessonUiState,
    onBack: () -> Unit,
    onTogglePlay: () -> Unit,
    onScrub: (Float) -> Unit,
    onScrubFinished: () -> Unit,
    onPreviousStep: () -> Unit,
    onNextStep: () -> Unit,
    onStartTrace: () -> Unit
) {
    val rope = ropeColorOf(state.ropeShade)

    ScreenBackground(
        overlay = listOf(
            ChartBase.copy(alpha = 0.80f),
            ChartMid.copy(alpha = 0.93f)
        ),
        imageAlpha = 0.45f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.desc_back),
                        tint = MistBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = state.knotName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Sailcloth,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                val playLabel = if (state.playing) {
                    stringResource(R.string.lesson_playing)
                } else {
                    stringResource(R.string.lesson_paused)
                }
                IconButton(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { stateDescription = playLabel }
                ) {
                    Image(
                        painter = painterResource(R.drawable.icon_pause),
                        contentDescription = if (state.playing) {
                            stringResource(R.string.desc_pause_animation)
                        } else {
                            stringResource(R.string.desc_resume_animation)
                        },
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(ChartMid.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                RopeCanvas(
                    ghost = state.ghost,
                    traced = state.traced,
                    marks = state.marks,
                    ropeColor = rope,
                    accentColor = SignalRed,
                    corridorColor = rope,
                    corridorAlpha = 0f,
                    strokeScale = 1f,
                    tightenScale = 1f,
                    finger = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 78.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_step) +
                        STEP_GAP + (state.stepIndex + 1) + STEP_SEPARATOR + state.stepCount,
                    style = MaterialTheme.typography.labelSmall,
                    color = RopeGold,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
                Text(
                    text = state.instruction,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Sailcloth,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Slider(
                value = state.scrubFraction,
                onValueChange = onScrub,
                onValueChangeFinished = onScrubFinished,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Sailcloth,
                    activeTrackColor = RopeGold,
                    inactiveTrackColor = ChipNavy,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GameConfig.SCRUBBER_HEIGHT_DP.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(state.stepCount.coerceAtLeast(1)) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                when {
                                    index == state.stepIndex -> RopeGold
                                    index < state.stepIndex -> MistBlue
                                    else -> RuleBrass
                                },
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.lesson_hint),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                color = DeepMist
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    label = stringResource(R.string.cta_prev_step),
                    onClick = onPreviousStep,
                    enabled = state.hasPrevious,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    label = stringResource(R.string.cta_next_step),
                    onClick = onNextStep,
                    enabled = state.hasNext,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                label = stringResource(R.string.cta_start_trace),
                onClick = onStartTrace,
                height = 60,
                leadingIcon = R.drawable.ic_play
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private const val STEP_GAP = " "
private const val STEP_SEPARATOR = " / "
