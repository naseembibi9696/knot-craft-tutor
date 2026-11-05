package com.knotcraft.tutor.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.domain.model.Crossing
import com.knotcraft.tutor.domain.model.CrossingMark
import com.knotcraft.tutor.domain.model.KnotSpec
import com.knotcraft.tutor.domain.model.PointF2D
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.SettingsRepository
import com.knotcraft.tutor.domain.usecase.BuildKnotPathUseCase
import com.knotcraft.tutor.domain.usecase.EvaluateTraceUseCase
import com.knotcraft.tutor.domain.usecase.TracePathUseCase
import com.knotcraft.tutor.domain.usecase.TrimPathUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    knotId: String,
    knotRepository: KnotRepository,
    private val settingsRepository: SettingsRepository,
    private val buildKnotPath: BuildKnotPathUseCase,
    private val trimPath: TrimPathUseCase,
    private val tracePath: TracePathUseCase,
    private val evaluateTrace: EvaluateTraceUseCase
) : ViewModel() {

    private val spec: KnotSpec = knotRepository.byId(knotId) ?: knotRepository.all().first()

    private var finishing = false

    private var lastStrayAt = 0L

    private val _uiState = MutableStateFlow(initialState())

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val resultChannel = Channel<TraceResult>(Channel.BUFFERED)

    val results: Flow<TraceResult> = resultChannel.receiveAsFlow()

    fun onTouchDown(x: Float, y: Float, aspect: Float) {
        if (!_uiState.value.interactive) return
        _uiState.update { it.copy(phase = TracePhase.TRACING, corridorVisible = true) }
        sample(x, y, aspect)
    }

    fun onTouchMove(x: Float, y: Float, aspect: Float) {
        if (!_uiState.value.interactive) return
        sample(x, y, aspect)
    }

    fun onTouchUp() {
        _uiState.update { it.copy(corridorVisible = false, finger = null) }
    }

    fun onTap(x: Float, y: Float, aspect: Float) {
        if (!_uiState.value.interactive) return
        sample(x, y, aspect)
        _uiState.update { it.copy(finger = null) }
    }

    fun onReset() {
        if (finishing) return
        _uiState.value = initialState()
    }

    fun onEndTrace() {
        if (finishing) return
        finish(clean = false, immediate = true)
    }

    fun pause() {
        _uiState.update { it.copy(corridorVisible = false, finger = null) }
    }

    private fun sample(x: Float, y: Float, aspect: Float) {
        val current = _uiState.value
        val ghost = current.ghost
        if (ghost.size < 2) return
        val fromIndex = (current.progress * (ghost.size - 1)).toInt()
        val projection = tracePath.project(
            points = ghost,
            target = PointF2D(x, y),
            aspect = aspect,
            fromIndex = fromIndex,
            lookAhead = GameConfig.TRACE_LOOKAHEAD
        )
        if (projection.distance <= GameConfig.CORRIDOR_NORM) {
            advance(projection.fraction, PointF2D(x, y))
        } else {
            registerStray(PointF2D(x, y))
        }
    }

    private fun advance(fraction: Float, finger: PointF2D) {
        val before = _uiState.value
        val progress = maxOf(before.progress, fraction)
        val cleared = crossingsUpTo(progress)
        val gained = cleared > before.crossingsCleared
        _uiState.update { current ->
            current.copy(
                progress = progress,
                traced = trimPath(current.ghost, progress),
                marks = marksOf(current.ghost, progress),
                crossingsCleared = cleared,
                finger = finger,
                crossingTick = if (gained) current.crossingTick + 1 else current.crossingTick
            )
        }
        val after = _uiState.value
        if (after.progress >= GameConfig.COMPLETE_THRESHOLD &&
            after.crossingsCleared >= after.totalCrossings
        ) {
            finish(clean = after.strays == 0, immediate = false)
        }
    }

    private fun registerStray(finger: PointF2D) {
        val now = System.currentTimeMillis()
        if (now - lastStrayAt < GameConfig.STRAY_COOLDOWN_MS) {
            _uiState.update { it.copy(finger = finger) }
            return
        }
        lastStrayAt = now
        _uiState.update { current ->
            current.copy(
                strays = current.strays + 1,
                strayPulse = true,
                finger = finger,
                strayTick = current.strayTick + 1
            )
        }
        viewModelScope.launch {
            delay(GameConfig.STRAY_PULSE_MS)
            _uiState.update { it.copy(strayPulse = false) }
        }
        if (_uiState.value.strays >= GameConfig.MAX_STRAYS) {
            finish(clean = false, immediate = false)
        }
    }

    private fun finish(clean: Boolean, immediate: Boolean) {
        if (finishing) return
        finishing = true
        _uiState.update {
            it.copy(
                phase = if (clean) TracePhase.TIGHTENING else TracePhase.FAILED,
                corridorVisible = false,
                finger = null
            )
        }
        viewModelScope.launch {
            if (!immediate) {
                delay(if (clean) GameConfig.TIGHTEN_MS else GameConfig.FAIL_DELAY_MS)
            }
            val snapshot = _uiState.value
            val result = evaluateTrace(
                spec = spec,
                clean = clean,
                progress = snapshot.progress,
                strays = snapshot.strays,
                crossingsCleared = snapshot.crossingsCleared
            )
            resultChannel.send(result)
        }
    }

    private fun crossingsUpTo(progress: Float): Int =
        spec.crossings.count { progress >= it.at }

    private fun marksOf(ghost: List<PointF2D>, progress: Float): List<CrossingMark> =
        spec.crossings.mapNotNull { crossing: Crossing ->
            val position = trimPath.pointAt(ghost, crossing.at) ?: return@mapNotNull null
            CrossingMark(
                position = position,
                side = crossing.side,
                cleared = progress >= crossing.at
            )
        }

    private fun initialState(): GameUiState {
        finishing = false
        lastStrayAt = 0L
        val settings = settingsRepository.settings.value
        val ghost = buildKnotPath(
            template = spec.template,
            variation = spec.variation,
            samples = GameConfig.PATH_SAMPLES,
            mirrored = settings.mirrored
        )
        return GameUiState(
            knotId = spec.id,
            knotName = spec.name,
            ghost = ghost,
            traced = emptyList(),
            marks = marksOf(ghost, 0f),
            totalCrossings = spec.crossings.size,
            ropeShade = settings.ropeShade,
            hapticsEnabled = settings.haptics
        )
    }
}
