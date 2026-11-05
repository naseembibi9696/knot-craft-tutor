package com.knotcraft.tutor.presentation.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.domain.model.Crossing
import com.knotcraft.tutor.domain.model.CrossingMark
import com.knotcraft.tutor.domain.model.PointF2D
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.repository.SettingsRepository
import com.knotcraft.tutor.domain.usecase.BuildKnotPathUseCase
import com.knotcraft.tutor.domain.usecase.TrimPathUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LessonViewModel(
    knotId: String,
    knotRepository: KnotRepository,
    progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val buildKnotPath: BuildKnotPathUseCase,
    private val trimPath: TrimPathUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LessonUiState())

    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    private var crossings: List<Crossing> = emptyList()

    init {
        val spec = knotRepository.byId(knotId) ?: knotRepository.all().first()
        crossings = spec.crossings
        val settings = settingsRepository.settings.value
        val ghost = buildKnotPath(
            template = spec.template,
            variation = spec.variation,
            samples = GameConfig.PATH_SAMPLES,
            mirrored = settings.mirrored
        )
        _uiState.value = LessonUiState(
            knotId = spec.id,
            knotName = spec.name,
            steps = spec.steps,
            ghost = ghost,
            traced = emptyList(),
            marks = marksOf(ghost, 0f),
            ropeShade = settings.ropeShade
        )
        progressRepository.touch(spec.id)
        viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.LESSON_FRAME_MS)
                if (_uiState.value.playing) {
                    step()
                }
            }
        }
    }

    fun onTogglePlay() {
        _uiState.update { it.copy(playing = !it.playing) }
    }

    fun pause() {
        _uiState.update { it.copy(playing = false) }
    }

    fun onScrub(value: Float) {
        _uiState.update { current ->
            applyFraction(current.copy(playing = false), value.coerceIn(0f, 1f))
        }
    }

    fun onScrubFinished() {
        _uiState.update { current ->
            val count = current.stepCount
            if (count <= 1) return@update current
            val nearest = Math.round(current.scrubFraction * (count - 1)).coerceIn(0, count - 1)
            applyFraction(current, nearest.toFloat() / (count - 1).toFloat())
        }
    }

    fun onPreviousStep() {
        _uiState.update { current ->
            val count = current.stepCount
            if (count <= 1 || current.stepIndex == 0) return@update current
            applyFraction(
                current.copy(playing = false),
                (current.stepIndex - 1).toFloat() / (count - 1).toFloat()
            )
        }
    }

    fun onNextStep() {
        _uiState.update { current ->
            val count = current.stepCount
            if (count <= 1 || current.stepIndex >= count - 1) return@update current
            applyFraction(
                current.copy(playing = false),
                (current.stepIndex + 1).toFloat() / (count - 1).toFloat()
            )
        }
    }

    private fun step() {
        _uiState.update { current ->
            val speed = settingsRepository.settings.value.speedMultiplier
            val delta = GameConfig.LESSON_FRAME_MS.toFloat() / GameConfig.LESSON_SWEEP_MS * speed
            val next = current.scrubFraction + delta
            applyFraction(current, if (next >= 1f) 0f else next)
        }
    }

    private fun applyFraction(state: LessonUiState, fraction: Float): LessonUiState {
        val count = state.stepCount
        val index = if (count <= 1) {
            0
        } else {
            (fraction * (count - 1)).toInt().coerceIn(0, count - 1)
        }
        return state.copy(
            scrubFraction = fraction,
            stepIndex = index,
            traced = trimPath(state.ghost, fraction),
            marks = marksOf(state.ghost, fraction)
        )
    }

    private fun marksOf(ghost: List<PointF2D>, fraction: Float): List<CrossingMark> =
        crossings.mapNotNull { crossing ->
            val position = trimPath.pointAt(ghost, crossing.at) ?: return@mapNotNull null
            CrossingMark(
                position = position,
                side = crossing.side,
                cleared = fraction >= crossing.at
            )
        }
}
