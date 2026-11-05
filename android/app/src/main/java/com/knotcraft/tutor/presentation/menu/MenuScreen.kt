package com.knotcraft.tutor.presentation.menu

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartLift
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.ui.AccentRule
import com.knotcraft.tutor.core.ui.Hairline
import com.knotcraft.tutor.core.ui.KnotRowCard
import com.knotcraft.tutor.core.ui.PrimaryButton
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.core.ui.SecondaryButton
import com.knotcraft.tutor.core.ui.SectionRule
import com.knotcraft.tutor.presentation.common.familyLabelRes
import com.knotcraft.tutor.presentation.common.thumbFor

@Composable
fun MenuScreen(
    onStartLesson: (String) -> Unit,
    onFamilies: () -> Unit,
    onQuiz: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(
        key = "menu",
        factory = AppViewModelFactory()
    )
    val state by menuViewModel.uiState.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onStartLesson = onStartLesson,
        onFamilies = onFamilies,
        onQuiz = onQuiz,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onStartLesson: (String) -> Unit,
    onFamilies: () -> Unit,
    onQuiz: () -> Unit,
    onSettings: () -> Unit
) {
    val fill by animateFloatAsState(
        targetValue = state.learnedFraction,
        animationSpec = tween(700),
        label = "learnedFill"
    )

    ScreenBackground(
        overlay = listOf(
            ChartLift.copy(alpha = 0.55f),
            ChartBase.copy(alpha = 0.86f),
            ChartMid.copy(alpha = 0.96f)
        ),
        imageAlpha = 0.6f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = stringResource(R.string.desc_locker),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.menu_eyebrow),
                    style = MaterialTheme.typography.labelSmall,
                    color = MistBlue
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.desc_settings),
                        tint = MistBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.desc_coiled_rope),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 8.dp, y = (-6).dp)
                        .size(140.dp)
                        .alpha(0.95f)
                )
                Column(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.menu_hero_one),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Sailcloth
                    )
                    Text(
                        text = stringResource(R.string.menu_hero_two),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        color = Sailcloth
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AccentRule(width = 72, accent = RopeGold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.menu_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        letterSpacing = 1.5.sp,
                        color = MistBlue,
                        maxLines = 2,
                        modifier = Modifier.width(190.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Hairline()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.menu_learned),
                        style = MaterialTheme.typography.labelSmall,
                        color = MistBlue
                    )
                    Text(
                        text = state.learnedCount.toString() + SEPARATOR + state.totalKnots,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = RopeGold,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(6.dp)
                        .background(ChipNavy, RoundedCornerShape(3.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fill.coerceIn(0f, 1f))
                            .height(6.dp)
                            .background(
                                Brush.horizontalGradient(listOf(RopeGold, Sailcloth)),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
            Hairline()

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                if (state.recent.isEmpty()) {
                    Text(
                        text = if (state.isLoading) {
                            stringResource(R.string.menu_loading)
                        } else {
                            stringResource(R.string.menu_first_hint)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp,
                        color = DeepMist
                    )
                } else {
                    SectionRule(title = stringResource(R.string.menu_recent))
                    Spacer(modifier = Modifier.height(10.dp))
                    state.recent.take(2).forEach { summary ->
                        KnotRowCard(
                            name = summary.name,
                            caption = stringResource(familyLabelRes(summary.family)),
                            streak = summary.cleanStreak,
                            learned = summary.learned,
                            learnedLabel = stringResource(R.string.tag_learned),
                            thumb = thumbFor(summary.spriteSlot),
                            thumbDescription = stringResource(R.string.desc_knot_thumb),
                            stateLabel = if (summary.learned) {
                                stringResource(R.string.tag_learned)
                            } else {
                                stringResource(R.string.state_not_selected)
                            },
                            onClick = { onStartLesson(summary.id) },
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }

            PrimaryButton(
                label = stringResource(R.string.cta_play),
                onClick = { onStartLesson(state.nextKnotId) },
                enabled = state.nextKnotId.isNotEmpty(),
                height = 60,
                leadingIcon = R.drawable.ic_play
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    label = stringResource(R.string.cta_families),
                    onClick = onFamilies,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    label = stringResource(R.string.cta_quiz),
                    onClick = onQuiz,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private const val SEPARATOR = " / "
