package com.knotcraft.tutor.presentation.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.usecase.BuildQuizUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    buildQuiz: BuildQuizUseCase,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizUiState(
            questions = buildQuiz(
                count = GameConfig.QUIZ_QUESTIONS,
                preferred = progressRepository.snapshot.value.recentIds,
                seed = System.currentTimeMillis()
            )
        )
    )

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val resultChannel = Channel<TraceResult>(Channel.BUFFERED)

    val results: Flow<TraceResult> = resultChannel.receiveAsFlow()

    private var finishing = false

    fun onAnswer(optionIndex: Int) {
        val current = _uiState.value
        if (current.revealed || current.current == null || finishing) return
        val correct = optionIndex == current.current?.correctIndex
        _uiState.update {
            it.copy(
                selectedIndex = optionIndex,
                revealed = true,
                score = if (correct) it.score + 1 else it.score
            )
        }
        viewModelScope.launch {
            delay(GameConfig.ANSWER_LOCK_MS)
            advance()
        }
    }

    private fun advance() {
        val current = _uiState.value
        if (current.index + 1 >= current.total) {
            complete(current.score, current.total)
            return
        }
        _uiState.update {
            it.copy(index = it.index + 1, selectedIndex = -1, revealed = false)
        }
    }

    private fun complete(score: Int, total: Int) {
        if (finishing) return
        finishing = true
        progressRepository.saveQuizBest(score)
        val accuracy = if (total == 0) 0 else score * 100 / total
        viewModelScope.launch {
            resultChannel.send(
                TraceResult(
                    knotId = "",
                    knotName = "",
                    clean = total > 0 && score * 2 >= total,
                    accuracy = accuracy,
                    crossingsCleared = score,
                    totalCrossings = total,
                    cleanStreak = 0,
                    justLearned = false,
                    quizMode = true,
                    quizScore = score,
                    quizTotal = total
                )
            )
        }
    }
}
