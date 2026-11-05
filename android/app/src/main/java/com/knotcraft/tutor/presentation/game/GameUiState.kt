package com.knotcraft.tutor.presentation.game

import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.domain.model.CrossingMark
import com.knotcraft.tutor.domain.model.PointF2D
import com.knotcraft.tutor.domain.model.RopeShade

enum class TracePhase { IDLE, TRACING, TIGHTENING, FAILED }

data class GameUiState(
    val knotId: String = "",
    val knotName: String = "",
    val ghost: List<PointF2D> = emptyList(),
    val traced: List<PointF2D> = emptyList(),
    val marks: List<CrossingMark> = emptyList(),
    val progress: Float = 0f,
    val strays: Int = 0,
    val crossingsCleared: Int = 0,
    val totalCrossings: Int = 0,
    val phase: TracePhase = TracePhase.IDLE,
    val corridorVisible: Boolean = false,
    val strayPulse: Boolean = false,
    val finger: PointF2D? = null,
    val ropeShade: RopeShade = RopeShade.MANILA,
    val hapticsEnabled: Boolean = true,
    val crossingTick: Int = 0,
    val strayTick: Int = 0
) {
    val percent: Int get() = (progress.coerceIn(0f, 1f) * 100f).toInt()

    val maxStrays: Int get() = GameConfig.MAX_STRAYS

    val interactive: Boolean
        get() = phase == TracePhase.IDLE || phase == TracePhase.TRACING
}
