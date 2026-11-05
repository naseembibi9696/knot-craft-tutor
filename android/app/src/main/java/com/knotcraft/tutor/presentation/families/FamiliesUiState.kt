package com.knotcraft.tutor.presentation.families

import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.KnotSummary

data class FamiliesUiState(
    val families: List<KnotFamily> = KnotFamily.entries,
    val selectedIndex: Int = 0,
    val items: List<KnotSummary> = emptyList()
)
