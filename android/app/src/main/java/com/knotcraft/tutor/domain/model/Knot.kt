package com.knotcraft.tutor.domain.model

enum class KnotFamily { LOOPS, HITCHES, BENDS, STOPPERS }

data class KnotSpec(
    val id: String,
    val name: String,
    val family: KnotFamily,
    val difficulty: Int,
    val use: String,
    val steps: List<String>,
    val template: PathTemplate,
    val variation: Int,
    val crossings: List<Crossing>
)

data class KnotProgress(
    val knotId: String,
    val cleanStreak: Int = 0,
    val learned: Boolean = false,
    val attempts: Int = 0,
    val bestAccuracy: Int = 0
)

data class KnotSummary(
    val id: String,
    val name: String,
    val family: KnotFamily,
    val use: String,
    val difficulty: Int,
    val cleanStreak: Int,
    val learned: Boolean,
    val spriteSlot: Int
)

data class ProgressSnapshot(
    val entries: Map<String, KnotProgress> = emptyMap(),
    val recentIds: List<String> = emptyList(),
    val quizBest: Int = 0
) {
    fun of(knotId: String): KnotProgress = entries[knotId] ?: KnotProgress(knotId)
    val learnedCount: Int get() = entries.values.count { it.learned }
}
