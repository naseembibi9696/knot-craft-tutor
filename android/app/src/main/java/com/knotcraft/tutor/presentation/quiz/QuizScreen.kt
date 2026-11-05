package com.knotcraft.tutor.presentation.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.presentation.common.quizSpriteFor

@Composable
fun QuizScreen(
    onBack: () -> Unit,
    onFinished: (TraceResult) -> Unit
) {
    val quizViewModel: QuizViewModel = viewModel(
        key = "quiz",
        factory = AppViewModelFactory()
    )
    val state by quizViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        quizViewModel.results.collect { result -> onFinished(result) }
    }

    QuizContent(
        state = state,
        onBack = onBack,
        onAnswer = quizViewModel::onAnswer
    )
}

@Composable
private fun QuizContent(
    state: QuizUiState,
    onBack: () -> Unit,
    onAnswer: (Int) -> Unit
) {
    val question = state.current

    ScreenBackground(
        overlay = listOf(
            ChartBase.copy(alpha = 0.84f),
            ChartMid.copy(alpha = 0.95f)
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
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.title_quiz),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Sailcloth
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.label_question) + GAP +
                        (state.index + 1) + SLASH + state.total,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = RopeGold,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }

            if (question == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.families_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MistBlue
                    )
                }
            } else {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.quiz_prompt),
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 15.sp,
                    color = Sailcloth
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(ChipNavy.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                        .border(1.dp, RuleBrass, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(quizSpriteFor(question.spriteSlot)),
                        contentDescription = stringResource(R.string.desc_quiz_knot),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(140.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    question.options.forEachIndexed { index, option ->
                        val isCorrect = index == question.correctIndex
                        val isChosen = index == state.selectedIndex
                        val border = when {
                            state.revealed && isCorrect -> RopeGold
                            state.revealed && isChosen -> SignalRed
                            else -> RuleBrass
                        }
                        val badge = when {
                            state.revealed && isCorrect -> RopeGold
                            state.revealed && isChosen -> SignalRed
                            else -> MistBlue
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .background(ChartMid.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                .border(
                                    if (state.revealed && (isCorrect || isChosen)) 1.5.dp else 1.dp,
                                    border,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !state.revealed) { onAnswer(index) }
                                .alpha(if (state.revealed && !isCorrect && !isChosen) 0.45f else 1f)
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(badge, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = BADGES[index % BADGES.size],
                                    style = MaterialTheme.typography.labelMedium,
                                    fontSize = 12.sp,
                                    color = ChartBase
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 14.sp,
                                color = Sailcloth,
                                maxLines = 3,
                                modifier = Modifier.weight(1f)
                            )
                            if (state.revealed && (isCorrect || isChosen)) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isCorrect) TICK else CROSS,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isCorrect) RopeGold else SignalRed
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.stat_score) + GAP + state.score,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = RopeGold
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .background(ChipNavy, RoundedCornerShape(3.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(
                                    if (state.total == 0) {
                                        0f
                                    } else {
                                        (state.index + 1).toFloat() / state.total.toFloat()
                                    }
                                )
                                .height(6.dp)
                                .background(RopeGold, RoundedCornerShape(3.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

private val BADGES = listOf("A", "B", "C", "D")
private const val GAP = " "
private const val SLASH = "/"
private const val TICK = "✓"
private const val CROSS = "✗"
