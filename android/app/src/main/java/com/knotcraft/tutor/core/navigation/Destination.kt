package com.knotcraft.tutor.core.navigation

import com.knotcraft.tutor.domain.model.TraceResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Families : Screen

    data object Quiz : Screen

    data object Settings : Screen

    data class Lesson(val knotId: String) : Screen

    data class Game(val knotId: String, val runId: Long) : Screen

    data class GameOver(val result: TraceResult, val runId: Long) : Screen
}
