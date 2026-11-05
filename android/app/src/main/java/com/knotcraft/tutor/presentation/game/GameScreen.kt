package com.knotcraft.tutor.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
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
import com.knotcraft.tutor.core.theme.ChartDeep
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.RopeLight
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.core.ui.PrimaryButton
import com.knotcraft.tutor.core.ui.RopeCanvas
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.core.ui.SecondaryButton
import com.knotcraft.tutor.core.ui.StatEntry
import com.knotcraft.tutor.core.ui.StatRow
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.presentation.common.ropeColorOf

@Composable
fun GameScreen(
    knotId: String,
    runId: Long,
    onBack: () -> Unit,
    onShowSteps: () -> Unit,
    onFinished: (TraceResult) -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        key = "game_" + knotId + "_" + runId,
        factory = AppViewModelFactory(knotId = knotId)
    )
    val state by gameViewModel.uiState.collectAsStateWithLifecycle()
    val haptics = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        gameViewModel.results.collect { result -> onFinished(result) }
    }

    DisposableEffect(runId) {
        onDispose { gameViewModel.pause() }
    }

    LaunchedEffect(state.crossingTick) {
        if (state.crossingTick > 0 && state.hapticsEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(state.strayTick) {
        if (state.strayTick > 0 && state.hapticsEnabled) {
            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    GameContent(
        state = state,
        onBack = onBack,
        onShowSteps = onShowSteps,
        onReset = gameViewModel::onReset,
        onEndTrace = gameViewModel::onEndTrace,
        onTouchDown = gameViewModel::onTouchDown,
        onTouchMove = gameViewModel::onTouchMove,
        onTouchUp = gameViewModel::onTouchUp,
        onTap = gameViewModel::onTap
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onBack: () -> Unit,
    onShowSteps: () -> Unit,
    onReset: () -> Unit,
    onEndTrace: () -> Unit,
    onTouchDown: (Float, Float, Float) -> Unit,
    onTouchMove: (Float, Float, Float) -> Unit,
    onTouchUp: () -> Unit,
    onTap: (Float, Float, Float) -> Unit
) {
    val tightening = state.phase == TracePhase.TIGHTENING
    val tighten by animateFloatAsState(
        targetValue = if (tightening) 0.94f else 1f,
        animationSpec = tween(GameConfig.TIGHTEN_MS.toInt()),
        label = "tighten"
    )
    val strokeScale by animateFloatAsState(
        targetValue = if (tightening) 0.64f else 1f,
        animationSpec = tween(GameConfig.TIGHTEN_MS.toInt()),
        label = "ropeStroke"
    )
    val rope = when (state.phase) {
        TracePhase.TIGHTENING -> RopeLight
        TracePhase.FAILED -> SignalRed
        else -> ropeColorOf(state.ropeShade)
    }
    val corridorAlpha = when {
        state.strayPulse -> 0.30f
        state.corridorVisible -> 0.10f
        else -> 0f
    }
    val interactive = state.interactive

    ScreenBackground(
        overlay = listOf(
            ChartDeep.copy(alpha = 0.82f),
            ChartBase.copy(alpha = 0.90f),
            ChartMid.copy(alpha = 0.95f)
        ),
        imageAlpha = 0.4f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
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
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 13.sp,
                    color = MistBlue,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.label_crossings) + GAP +
                        state.crossingsCleared + SLASH + state.totalCrossings,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RopeGold,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                RopeCanvas(
                    ghost = state.ghost,
                    traced = state.traced,
                    marks = state.marks,
                    ropeColor = rope,
                    accentColor = SignalRed,
                    corridorColor = if (state.strayPulse) SignalRed else rope,
                    corridorAlpha = corridorAlpha,
                    strokeScale = strokeScale,
                    tightenScale = tighten,
                    finger = state.finger,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(state.knotId, interactive) {
                            if (!interactive) return@pointerInput
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            if (w <= 0f || h <= 0f) return@pointerInput
                            detectDragGestures(
                                onDragStart = { offset ->
                                    onTouchDown(offset.x / w, offset.y / h, h / w)
                                },
                                onDrag = { change, _ ->
                                    onTouchMove(
                                        change.position.x / w,
                                        change.position.y / h,
                                        h / w
                                    )
                                },
                                onDragEnd = { onTouchUp() },
                                onDragCancel = { onTouchUp() }
                            )
                        }
                        .pointerInput(state.knotId, interactive) {
                            if (!interactive) return@pointerInput
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            if (w <= 0f || h <= 0f) return@pointerInput
                            detectTapGestures { offset ->
                                onTap(offset.x / w, offset.y / h, h / w)
                            }
                        }
                )
            }

            StatRow(
                cards = listOf(
                    StatEntry(
                        label = stringResource(R.string.stat_progress),
                        value = state.percent.toString() + PERCENT,
                        accent = RopeGold,
                        visible = state.percent > 0
                    ),
                    StatEntry(
                        label = stringResource(R.string.stat_clean),
                        value = state.crossingsCleared.toString(),
                        accent = RopeLight,
                        visible = state.crossingsCleared > 0
                    ),
                    StatEntry(
                        label = stringResource(R.string.stat_strays),
                        value = state.strays.toString() + SLASH + state.maxStrays,
                        accent = SignalRed,
                        visible = state.strays > 0
                    )
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Surface(
                color = ChartMid.copy(alpha = 0.92f),
                shape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(RuleBrass)
                    )
                    Text(
                        text = stringResource(R.string.game_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = MistBlue,
                        maxLines = 2
                    )
                    PrimaryButton(
                        label = stringResource(R.string.cta_reset_trace),
                        onClick = onReset,
                        enabled = interactive,
                        height = 56
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            label = stringResource(R.string.cta_show_steps),
                            onClick = onShowSteps,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            label = stringResource(R.string.cta_end_trace),
                            onClick = onEndTrace,
                            enabled = interactive,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

private const val GAP = " "
private const val SLASH = "/"
private const val PERCENT = "%"
