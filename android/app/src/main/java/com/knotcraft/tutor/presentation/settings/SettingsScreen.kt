package com.knotcraft.tutor.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.core.ui.ChipRow
import com.knotcraft.tutor.core.ui.Hairline
import com.knotcraft.tutor.core.ui.ScreenBackground
import com.knotcraft.tutor.domain.model.RopeShade
import com.knotcraft.tutor.presentation.dialog.ResetProgressDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(
        key = "settings",
        factory = AppViewModelFactory()
    )
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onBack = onBack,
        onToggleMirror = settingsViewModel::onToggleMirror,
        onSelectShade = settingsViewModel::onSelectShade,
        onSelectSpeed = settingsViewModel::onSelectSpeed,
        onToggleHaptics = settingsViewModel::onToggleHaptics,
        onOpenResetDialog = settingsViewModel::onOpenResetDialog,
        onDismissResetDialog = settingsViewModel::onDismissResetDialog,
        onConfirmReset = settingsViewModel::onConfirmReset
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onToggleMirror: (Boolean) -> Unit,
    onSelectShade: (Int) -> Unit,
    onSelectSpeed: (Int) -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onOpenResetDialog: () -> Unit,
    onDismissResetDialog: () -> Unit,
    onConfirmReset: () -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)
    val selectedLabel = stringResource(R.string.state_selected)
    val unselectedLabel = stringResource(R.string.state_not_selected)
    val shadeLabels = listOf(
        stringResource(R.string.shade_manila),
        stringResource(R.string.shade_signal),
        stringResource(R.string.shade_sailcloth)
    )
    val speedLabels = listOf(
        stringResource(R.string.speed_slow),
        stringResource(R.string.speed_normal),
        stringResource(R.string.speed_fast)
    )
    val scrollState = rememberScrollState()

    if (state.resetDialogVisible) {
        ResetProgressDialog(
            onConfirm = onConfirmReset,
            onDismiss = onDismissResetDialog
        )
    }

    ScreenBackground(
        overlay = listOf(
            ChartBase.copy(alpha = 0.86f),
            ChartMid.copy(alpha = 0.95f)
        ),
        imageAlpha = 0.4f
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
                    text = stringResource(R.string.title_settings),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Sailcloth
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .semantics {
                            stateDescription = if (state.settings.mirrored) onLabel else offLabel
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_mirror),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 15.sp,
                            color = Sailcloth
                        )
                        Text(
                            text = stringResource(R.string.settings_mirror_help),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = DeepMist
                        )
                    }
                    Switch(
                        checked = state.settings.mirrored,
                        onCheckedChange = onToggleMirror,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = RopeGold,
                            checkedThumbColor = ChartBase,
                            uncheckedTrackColor = ChipNavy,
                            uncheckedThumbColor = MistBlue
                        )
                    )
                }

                Hairline()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.settings_shade),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        color = Sailcloth
                    )
                    Text(
                        text = stringResource(R.string.settings_shade_help),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = DeepMist
                    )
                    ChipRow(
                        options = shadeLabels,
                        selectedIndex = remember(state.settings.ropeShade) {
                            RopeShade.entries.indexOf(state.settings.ropeShade)
                        },
                        selectedLabel = selectedLabel,
                        unselectedLabel = unselectedLabel,
                        onSelect = onSelectShade
                    )
                }

                Hairline()

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.settings_speed),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 15.sp,
                        color = Sailcloth
                    )
                    Text(
                        text = stringResource(R.string.settings_speed_help),
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = DeepMist
                    )
                    ChipRow(
                        options = speedLabels,
                        selectedIndex = state.settings.speedIndex,
                        selectedLabel = selectedLabel,
                        unselectedLabel = unselectedLabel,
                        onSelect = onSelectSpeed
                    )
                }

                Hairline()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp)
                        .semantics {
                            stateDescription = if (state.settings.haptics) onLabel else offLabel
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_haptics),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 15.sp,
                            color = Sailcloth
                        )
                        Text(
                            text = stringResource(R.string.settings_haptics_help),
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 12.sp,
                            color = DeepMist
                        )
                    }
                    Switch(
                        checked = state.settings.haptics,
                        onCheckedChange = onToggleHaptics,
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = RopeGold,
                            checkedThumbColor = ChartBase,
                            uncheckedTrackColor = ChipNavy,
                            uncheckedThumbColor = MistBlue
                        )
                    )
                }

                Hairline()

                Text(
                    text = stringResource(R.string.settings_danger),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = DeepMist
                )

                TextButton(
                    onClick = onOpenResetDialog,
                    modifier = Modifier.heightIn(min = 48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cta_reset_progress),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 14.sp,
                        color = SignalRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
