package com.knotcraft.tutor.presentation.families

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.ui.ChipRow
import com.knotcraft.tutor.core.ui.KnotRowCard
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.presentation.common.difficultyMark
import com.knotcraft.tutor.presentation.common.thumbFor

@Composable
fun FamiliesScreen(
    onBack: () -> Unit,
    onOpenKnot: (String) -> Unit
) {
    val familiesViewModel: FamiliesViewModel = viewModel(
        key = "families",
        factory = AppViewModelFactory()
    )
    val state by familiesViewModel.uiState.collectAsStateWithLifecycle()

    FamiliesContent(
        state = state,
        onBack = onBack,
        onSelectFamily = familiesViewModel::onSelectFamily,
        onOpenKnot = onOpenKnot
    )
}

@Composable
private fun FamiliesContent(
    state: FamiliesUiState,
    onBack: () -> Unit,
    onSelectFamily: (Int) -> Unit,
    onOpenKnot: (String) -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            ChartBase.copy(alpha = 0.82f),
            ChartMid.copy(alpha = 0.94f)
        ),
        imageAlpha = 0.5f
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
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.title_families),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Sailcloth
                )
            }

            val familyLabels = listOf(
                stringResource(R.string.family_loops),
                stringResource(R.string.family_hitches),
                stringResource(R.string.family_bends),
                stringResource(R.string.family_stoppers)
            )

            ChipRow(
                options = familyLabels,
                selectedIndex = state.selectedIndex,
                selectedLabel = stringResource(R.string.state_selected),
                unselectedLabel = stringResource(R.string.state_not_selected),
                onSelect = onSelectFamily
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (state.items.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.families_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 13.sp,
                        color = DeepMist
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = state.items, key = { it.id }) { summary ->
                        KnotRowCard(
                            name = summary.name,
                            caption = summary.use,
                            streak = summary.cleanStreak,
                            learned = summary.learned,
                            learnedLabel = stringResource(R.string.tag_learned),
                            thumb = thumbFor(summary.spriteSlot),
                            thumbDescription = stringResource(R.string.desc_knot_thumb),
                            stateLabel = difficultyMark(summary.difficulty),
                            onClick = { onOpenKnot(summary.id) }
                        )
                    }
                }
            }
        }
    }
}
