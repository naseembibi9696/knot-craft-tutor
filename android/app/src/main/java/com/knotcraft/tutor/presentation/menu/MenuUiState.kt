package com.knotcraft.tutor.presentation.menu

import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.domain.model.KnotSummary

data class MenuUiState(
    val isLoading: Boolean = true,
    val learnedCount: Int = 0,
    val totalKnots: Int = GameConfig.TOTAL_KNOTS,
    val recent: List<KnotSummary> = emptyList(),
    val nextKnotId: String = ""
) {
    val learnedFraction: Float
        get() = if (totalKnots == 0) 0f else learnedCount.toFloat() / totalKnots.toFloat()
}
