package com.knotcraft.tutor.presentation.gameover

import androidx.lifecycle.ViewModel
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.usecase.GetNextKnotUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameOverUiState(
    val result: TraceResult,
    val nextKnotId: String,
    val replayKnotId: String,
    val quizBest: Int,
    val ropeCheckVisible: Boolean,
    val ropeCheckDone: Boolean = false,
    val checks: List<Boolean> = listOf(false, false, false, false)
) {
    val allChecked: Boolean get() = checks.all { it }
}

class GameOverViewModel(
    result: TraceResult,
    progressRepository: ProgressRepository,
    nextKnot: GetNextKnotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildState(result, progressRepository, nextKnot))

    val uiState: StateFlow<GameOverUiState> = _uiState.asStateFlow()

    fun onOpenRopeCheck() {
        _uiState.update { it.copy(ropeCheckVisible = true) }
    }

    fun onDismissRopeCheck() {
        _uiState.update { it.copy(ropeCheckVisible = false) }
    }

    fun onToggleCheck(index: Int) {
        _uiState.update { current ->
            val updated = current.checks.toMutableList()
            if (index in updated.indices) {
                updated[index] = !updated[index]
            }
            current.copy(checks = updated.toList())
        }
    }

    fun onConfirmRopeCheck() {
        _uiState.update { it.copy(ropeCheckVisible = false, ropeCheckDone = true) }
    }

    private companion object {
        fun buildState(
            result: TraceResult,
            progressRepository: ProgressRepository,
            nextKnot: GetNextKnotUseCase
        ): GameOverUiState {
            val next = nextKnot(result.knotId.ifEmpty { null })?.id.orEmpty()
            return GameOverUiState(
                result = result,
                nextKnotId = next,
                replayKnotId = result.knotId.ifEmpty { next },
                quizBest = progressRepository.snapshot.value.quizBest,
                ropeCheckVisible = result.justLearned
            )
        }
    }
}
