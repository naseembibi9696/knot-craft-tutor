package com.knotcraft.tutor.domain.model

enum class RopeShade { MANILA, SIGNAL, SAILCLOTH }

data class LockerSettings(
    val mirrored: Boolean = false,
    val ropeShade: RopeShade = RopeShade.MANILA,
    val speedIndex: Int = 1,
    val haptics: Boolean = true
) {
    val speedMultiplier: Float
        get() = when (speedIndex) {
            0 -> 0.5f
            2 -> 1.5f
            else -> 1.0f
        }
}
