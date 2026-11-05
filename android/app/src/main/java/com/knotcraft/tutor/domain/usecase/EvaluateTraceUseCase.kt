package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.KnotSpec
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.domain.repository.ProgressRepository

class EvaluateTraceUseCase(private val progressRepository: ProgressRepository) {

    operator fun invoke(
        spec: KnotSpec,
        clean: Boolean,
        progress: Float,
        strays: Int,
        crossingsCleared: Int
    ): TraceResult {
        val accuracy = accuracyOf(progress, strays)
        val before = progressRepository.progressOf(spec.id)
        val after = progressRepository.record(spec.id, clean, accuracy)
        return TraceResult(
            knotId = spec.id,
            knotName = spec.name,
            clean = clean,
            accuracy = accuracy,
            crossingsCleared = crossingsCleared,
            totalCrossings = spec.crossings.size,
            cleanStreak = after.cleanStreak,
            justLearned = after.learned && !before.learned
        )
    }

    private fun accuracyOf(progress: Float, strays: Int): Int {
        val base = progress.coerceIn(0f, 1f) * 100f
        val penalty = strays * 12f
        return (base - penalty).coerceIn(0f, 100f).toInt()
    }
}
