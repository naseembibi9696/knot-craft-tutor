package com.knotcraft.tutor.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.domain.model.KnotSummary
import com.knotcraft.tutor.domain.model.ProgressSnapshot
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.usecase.GetNextKnotUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val knotRepository: KnotRepository,
    private val progressRepository: ProgressRepository,
    private val nextKnot: GetNextKnotUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())

    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.snapshot.collect { snapshot ->
                val recent = recentOf(snapshot)
                val next = nextKnot(snapshot.recentIds.firstOrNull())
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        learnedCount = snapshot.learnedCount,
                        totalKnots = knotRepository.all().size,
                        recent = recent,
                        nextKnotId = next?.id.orEmpty()
                    )
                }
            }
        }
    }

    private fun recentOf(snapshot: ProgressSnapshot): List<KnotSummary> =
        snapshot.recentIds.mapIndexedNotNull { index, id ->
            val spec = knotRepository.byId(id) ?: return@mapIndexedNotNull null
            val progress = snapshot.of(id)
            KnotSummary(
                id = spec.id,
                name = spec.name,
                family = spec.family,
                use = spec.use,
                difficulty = spec.difficulty,
                cleanStreak = progress.cleanStreak,
                learned = progress.learned,
                spriteSlot = index % 3
            )
        }
}
