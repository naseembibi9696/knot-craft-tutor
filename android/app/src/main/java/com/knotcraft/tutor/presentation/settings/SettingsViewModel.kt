package com.knotcraft.tutor.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.domain.model.RopeShade
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(settingsRepository.settings.value))

    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _uiState.update { it.copy(settings = settings) }
            }
        }
    }

    fun onToggleMirror(value: Boolean) {
        settingsRepository.setMirrored(value)
    }

    fun onSelectShade(index: Int) {
        val shades = RopeShade.entries
        settingsRepository.setRopeShade(shades[index.coerceIn(0, shades.lastIndex)])
    }

    fun onSelectSpeed(index: Int) {
        settingsRepository.setSpeedIndex(index)
    }

    fun onToggleHaptics(value: Boolean) {
        settingsRepository.setHaptics(value)
    }

    fun onOpenResetDialog() {
        _uiState.update { it.copy(resetDialogVisible = true) }
    }

    fun onDismissResetDialog() {
        _uiState.update { it.copy(resetDialogVisible = false) }
    }

    fun onConfirmReset() {
        progressRepository.reset()
        _uiState.update { it.copy(resetDialogVisible = false) }
    }
}
