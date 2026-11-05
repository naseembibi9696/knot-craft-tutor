package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.QuizQuestion
import com.knotcraft.tutor.domain.repository.KnotRepository
import kotlin.random.Random

class BuildQuizUseCase(private val knotRepository: KnotRepository) {

    operator fun invoke(count: Int, preferred: List<String>, seed: Long): List<QuizQuestion> {
        val all = knotRepository.all()
        if (all.size < 4) return emptyList()
        val random = Random(seed)
        val front = all.filter { preferred.contains(it.id) }.shuffled(random)
        val pool = (front + all.shuffled(random)).distinctBy { it.id }.take(count)
        return pool.mapIndexed { index, spec ->
            val distractors = all
                .filter { it.id != spec.id && it.use != spec.use }
                .shuffled(random)
                .take(3)
                .map { it.use }
            val options = (distractors + spec.use).shuffled(random)
            QuizQuestion(
                knotId = spec.id,
                knotName = spec.name,
                options = options,
                correctIndex = options.indexOf(spec.use),
                spriteSlot = index % 3
            )
        }
    }
}
