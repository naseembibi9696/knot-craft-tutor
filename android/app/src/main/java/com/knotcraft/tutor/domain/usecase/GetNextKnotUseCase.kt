package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.KnotSpec
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.ProgressRepository

class GetNextKnotUseCase(
    private val knotRepository: KnotRepository,
    private val progressRepository: ProgressRepository
) {

    operator fun invoke(after: String?): KnotSpec? {
        val all = knotRepository.all()
        if (all.isEmpty()) return null
        val startIndex = all.indexOfFirst { it.id == after } + 1
        val ordered = all.drop(startIndex) + all.take(startIndex)
        return ordered.firstOrNull { !progressRepository.progressOf(it.id).learned } ?: ordered.first()
    }
}
