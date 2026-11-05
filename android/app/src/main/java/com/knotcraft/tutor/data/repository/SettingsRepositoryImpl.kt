package com.knotcraft.tutor.data.repository

import com.knotcraft.tutor.data.local.KnotPreferences
import com.knotcraft.tutor.domain.model.LockerSettings
import com.knotcraft.tutor.domain.model.RopeShade
import com.knotcraft.tutor.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(private val preferences: KnotPreferences) : SettingsRepository {

    private val _settings = MutableStateFlow(read())

    override val settings: StateFlow<LockerSettings> = _settings.asStateFlow()

    override fun setMirrored(value: Boolean) {
        preferences.writeMirrored(value)
        _settings.value = read()
    }

    override fun setRopeShade(value: RopeShade) {
        preferences.writeRopeShadeIndex(RopeShade.entries.indexOf(value))
        _settings.value = read()
    }

    override fun setSpeedIndex(value: Int) {
        preferences.writeSpeedIndex(value.coerceIn(0, 2))
        _settings.value = read()
    }

    override fun setHaptics(value: Boolean) {
        preferences.writeHaptics(value)
        _settings.value = read()
    }

    private fun read(): LockerSettings {
        val shades = RopeShade.entries
        val index = preferences.ropeShadeIndex().coerceIn(0, shades.lastIndex)
        return LockerSettings(
            mirrored = preferences.mirrored(),
            ropeShade = shades[index],
            speedIndex = preferences.speedIndex().coerceIn(0, 2),
            haptics = preferences.haptics()
        )
    }
}
