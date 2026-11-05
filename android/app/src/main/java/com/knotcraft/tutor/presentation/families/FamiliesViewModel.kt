package com.knotcraft.tutor.presentation.families

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.KnotSummary
import com.knotcraft.tutor.domain.model.ProgressSnapshot
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FamiliesViewModel(
    private val knotRepository: KnotRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FamiliesUiState())

    val uiState: StateFlow<FamiliesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.snapshot.collect { snapshot ->
                _uiState.update { current ->
                    current.copy(items = itemsOf(current.families[current.selectedIndex], snapshot))
                }
            }
        }
    }

    fun onSelectFamily(index: Int) {
        _uiState.update { current ->
            val bounded = index.coerceIn(0, current.families.lastIndex)
            current.copy(
                selectedIndex = bounded,
                items = itemsOf(current.families[bounded], progressRepository.snapshot.value)
            )
        }
    }

    private fun itemsOf(family: KnotFamily, snapshot: ProgressSnapshot): List<KnotSummary> =
        knotRepository.byFamily(family).mapIndexed { index, spec ->
            val progress = snapshot.of(spec.id)
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
