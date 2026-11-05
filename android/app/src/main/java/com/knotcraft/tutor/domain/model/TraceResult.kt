package com.knotcraft.tutor.domain.model

data class TraceResult(
    val knotId: String,
    val knotName: String,
    val clean: Boolean,
    val accuracy: Int,
    val crossingsCleared: Int,
    val totalCrossings: Int,
    val cleanStreak: Int,
    val justLearned: Boolean,
    val quizMode: Boolean = false,
    val quizScore: Int = 0,
    val quizTotal: Int = 0
)
