package com.knotcraft.tutor.domain.repository

import com.knotcraft.tutor.domain.model.LockerSettings
import com.knotcraft.tutor.domain.model.RopeShade
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<LockerSettings>
    fun setMirrored(value: Boolean)
    fun setRopeShade(value: RopeShade)
    fun setSpeedIndex(value: Int)
    fun setHaptics(value: Boolean)
}
