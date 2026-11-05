package com.knotcraft.tutor.presentation.gameover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartLift
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.RopeLight
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.core.ui.AccentRule
import com.knotcraft.tutor.core.ui.PrimaryButton
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.core.ui.SecondaryButton
import com.knotcraft.tutor.core.ui.StatEntry
import com.knotcraft.tutor.core.ui.StatRow
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.presentation.dialog.RopeCheckDialog

@Composable
fun GameOverScreen(
    result: TraceResult,
    runId: Long,
    onTraceAgain: (String) -> Unit,
    onNextKnot: (String) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(
        key = "gameover_$runId",
        factory = AppViewModelFactory(knotId = result.knotId, result = result)
    )
    val state by gameOverViewModel.uiState.collectAsStateWithLifecycle()

    GameOverContent(
        state = state,
        onTraceAgain = onTraceAgain,
        onNextKnot = onNextKnot,
        onMenu = onMenu,
        onOpenRopeCheck = gameOverViewModel::onOpenRopeCheck,
        onDismissRopeCheck = gameOverViewModel::onDismissRopeCheck,
        onToggleCheck = gameOverViewModel::onToggleCheck,
        onConfirmRopeCheck = gameOverViewModel::onConfirmRopeCheck
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onTraceAgain: (String) -> Unit,
    onNextKnot: (String) -> Unit,
    onMenu: () -> Unit,
    onOpenRopeCheck: () -> Unit,
    onDismissRopeCheck: () -> Unit,
    onToggleCheck: (Int) -> Unit,
    onConfirmRopeCheck: () -> Unit
) {
    val result = state.result
    val success = result.clean
    val verdictColor = if (success) RopeGold else SignalRed
    val verdict = when {
        result.quizMode -> stringResource(R.string.verdict_quiz)
        success -> stringResource(R.string.verdict_tied)
        else -> stringResource(R.string.verdict_slipped)
    }
    val hero = if (success) R.drawable.sprite_3 else R.drawable.sprite_5

    if (state.ropeCheckVisible) {
        RopeCheckDialog(
            checks = state.checks,
            allChecked = state.allChecked,
            onToggle = onToggleCheck,
            onConfirm = onConfirmRopeCheck,
            onDismiss = onDismissRopeCheck
        )
    }

    ScreenBackground(
        overlay = listOf(
            ChartMid.copy(alpha = 0.86f),
            ChartBase.copy(alpha = 0.90f),
            ChartLift.copy(alpha = 0.92f)
        ),
        imageAlpha = 0.5f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Box(contentAlignment = Alignment.Center) {
                if (success) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        RopeGold.copy(alpha = 0.12f),
                                        RopeGold.copy(alpha = 0f)
                                    )
                                )
                            )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = verdict,
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = verdictColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    AccentRule(width = 72, accent = verdictColor)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (result.knotName.isNotEmpty()) {
                Text(
                    text = result.knotName,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    color = MistBlue,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(hero),
                contentDescription = stringResource(R.string.desc_result_knot),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(130.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            StatRow(
                cards = listOf(
                    StatEntry(
                        label = stringResource(R.string.stat_accuracy),
                        value = result.accuracy.toString() + PERCENT,
                        accent = RopeGold,
                        visible = result.accuracy > 0
                    ),
                    StatEntry(
                        label = if (result.quizMode) {
                            stringResource(R.string.stat_score)
                        } else {
                            stringResource(R.string.stat_crossings)
                        },
                        value = result.crossingsCleared.toString() + SLASH +
                            result.totalCrossings,
                        accent = RopeLight,
                        visible = result.totalCrossings > 0
                    ),
                    StatEntry(
                        label = stringResource(R.string.stat_clean_runs),
                        value = result.cleanStreak.toString() + SLASH +
                            GameConfig.CLEAN_RUNS_TO_LEARN,
                        accent = Sailcloth,
                        visible = !result.quizMode && result.cleanStreak > 0
                    )
                )
            )

            AnimatedVisibility(
                visible = result.justLearned,
                enter = scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) + fadeIn(tween(260))
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(RopeGold.copy(alpha = 0.16f), RoundedCornerShape(10.dp))
                        .border(1.dp, RopeGold, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.banner_learned),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 15.sp,
                        color = RopeGold
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = result.knotName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp,
                        color = Sailcloth,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (result.justLearned && !state.ropeCheckVisible) {
                SecondaryButton(
                    label = stringResource(R.string.dialog_rope_check),
                    onClick = onOpenRopeCheck,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            PrimaryButton(
                label = stringResource(R.string.cta_trace_again),
                onClick = { onTraceAgain(state.replayKnotId) },
                enabled = state.replayKnotId.isNotEmpty(),
                height = 60,
                leadingIcon = R.drawable.ic_play
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    label = stringResource(R.string.cta_next_knot),
                    onClick = { onNextKnot(state.nextKnotId) },
                    enabled = state.nextKnotId.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    label = stringResource(R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private const val SLASH = " / "
private const val PERCENT = "%"
