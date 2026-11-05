package com.knotcraft.tutor.presentation.quiz

import com.knotcraft.tutor.domain.model.QuizQuestion

data class QuizUiState(
    val questions: List<QuizQuestion> = emptyList(),
    val index: Int = 0,
    val score: Int = 0,
    val selectedIndex: Int = -1,
    val revealed: Boolean = false
) {
    val current: QuizQuestion? get() = questions.getOrNull(index)

    val total: Int get() = questions.size
}
