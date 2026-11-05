package com.knotcraft.tutor.domain.model

data class PointF2D(val x: Float, val y: Float)

enum class CrossingSide { OVER, UNDER }

data class Crossing(val at: Float, val side: CrossingSide)

data class CrossingMark(val position: PointF2D, val side: CrossingSide, val cleared: Boolean)

enum class PathTemplate {
    OVERHAND_LOOP,
    DOUBLE_LOOP,
    ROUND_TURN,
    FIGURE_EIGHT,
    CLOVE,
    SHEET_BEND,
    CLEAT,
    COIL_STOPPER
}
