package com.knotcraft.tutor.domain.model

data class QuizQuestion(
    val knotId: String,
    val knotName: String,
    val options: List<String>,
    val correctIndex: Int,
    val spriteSlot: Int
)
