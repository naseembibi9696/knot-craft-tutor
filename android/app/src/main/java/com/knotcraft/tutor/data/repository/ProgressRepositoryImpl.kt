package com.knotcraft.tutor.data.repository

import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.data.local.KnotPreferences
import com.knotcraft.tutor.data.sample.SampleData
import com.knotcraft.tutor.domain.model.KnotProgress
import com.knotcraft.tutor.domain.model.ProgressSnapshot
import com.knotcraft.tutor.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProgressRepositoryImpl(private val preferences: KnotPreferences) : ProgressRepository {

    private val _snapshot = MutableStateFlow(read())

    override val snapshot: StateFlow<ProgressSnapshot> = _snapshot.asStateFlow()

    override fun progressOf(knotId: String): KnotProgress = _snapshot.value.of(knotId)

    override fun record(knotId: String, clean: Boolean, accuracy: Int): KnotProgress {
        val current = progressOf(knotId)
        val streak = if (clean) {
            (current.cleanStreak + 1).coerceAtMost(GameConfig.CLEAN_RUNS_TO_LEARN)
        } else {
            0
        }
        val learned = current.learned || streak >= GameConfig.CLEAN_RUNS_TO_LEARN
        val updated = current.copy(
            cleanStreak = streak,
            learned = learned,
            attempts = current.attempts + 1,
            bestAccuracy = maxOf(current.bestAccuracy, accuracy)
        )
        preferences.writeProgress(knotId, streak, updated.attempts, updated.bestAccuracy, learned)
        preferences.pushRecent(knotId, RECENT_LIMIT)
        _snapshot.value = read()
        return updated
    }

    override fun touch(knotId: String) {
        preferences.pushRecent(knotId, RECENT_LIMIT)
        _snapshot.value = read()
    }

    override fun saveQuizBest(score: Int) {
        if (score <= preferences.quizBest()) return
        preferences.writeQuizBest(score)
        _snapshot.value = read()
    }

    override fun reset() {
        preferences.clearProgress()
        _snapshot.value = read()
    }

    private fun read(): ProgressSnapshot {
        val entries = SampleData.knots.associate { spec ->
            spec.id to KnotProgress(
                knotId = spec.id,
                cleanStreak = preferences.streakOf(spec.id),
                learned = preferences.isLearned(spec.id),
                attempts = preferences.attemptsOf(spec.id),
                bestAccuracy = preferences.bestAccuracyOf(spec.id)
            )
        }
        return ProgressSnapshot(
            entries = entries,
            recentIds = preferences.recentIds(),
            quizBest = preferences.quizBest()
        )
    }

    private companion object {
        const val RECENT_LIMIT = 3
    }
}
