package com.knotcraft.tutor.domain.repository

import com.knotcraft.tutor.domain.model.KnotProgress
import com.knotcraft.tutor.domain.model.ProgressSnapshot
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val snapshot: StateFlow<ProgressSnapshot>
    fun progressOf(knotId: String): KnotProgress
    fun record(knotId: String, clean: Boolean, accuracy: Int): KnotProgress
    fun touch(knotId: String)
    fun saveQuizBest(score: Int)
    fun reset()
}
