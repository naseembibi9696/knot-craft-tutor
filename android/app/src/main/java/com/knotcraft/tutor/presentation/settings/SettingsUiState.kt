package com.knotcraft.tutor.presentation.settings

import com.knotcraft.tutor.domain.model.LockerSettings

data class SettingsUiState(
    val settings: LockerSettings = LockerSettings(),
    val resetDialogVisible: Boolean = false
)
