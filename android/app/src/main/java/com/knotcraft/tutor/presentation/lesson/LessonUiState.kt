package com.knotcraft.tutor.presentation.lesson

import com.knotcraft.tutor.domain.model.CrossingMark
import com.knotcraft.tutor.domain.model.PointF2D
import com.knotcraft.tutor.domain.model.RopeShade

data class LessonUiState(
    val knotId: String = "",
    val knotName: String = "",
    val steps: List<String> = emptyList(),
    val stepIndex: Int = 0,
    val scrubFraction: Float = 0f,
    val playing: Boolean = true,
    val ghost: List<PointF2D> = emptyList(),
    val traced: List<PointF2D> = emptyList(),
    val marks: List<CrossingMark> = emptyList(),
    val ropeShade: RopeShade = RopeShade.MANILA
) {
    val stepCount: Int get() = steps.size

    val instruction: String get() = steps.getOrElse(stepIndex) { "" }

    val hasPrevious: Boolean get() = stepIndex > 0

    val hasNext: Boolean get() = stepIndex < stepCount - 1
}
