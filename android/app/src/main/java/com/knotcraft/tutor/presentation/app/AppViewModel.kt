package com.knotcraft.tutor.presentation.app

import androidx.lifecycle.ViewModel
import com.knotcraft.tutor.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {

    private val _state = MutableStateFlow(NavState())

    val state: StateFlow<NavState> = _state.asStateFlow()

    fun navigate(target: Screen) {
        _state.update { current ->
            val stack = if (current.screen is Screen.Splash) {
                current.backStack
            } else {
                current.backStack + current.screen
            }
            current.copy(screen = target, backStack = stack)
        }
    }

    fun navigateReplacing(target: Screen) {
        _state.update { current -> current.copy(screen = target) }
    }

    fun back() {
        _state.update { current ->
            val previous = current.backStack.lastOrNull() ?: return@update current
            current.copy(screen = previous, backStack = current.backStack.dropLast(1))
        }
    }

    fun home() {
        _state.update { NavState(screen = Screen.Menu, backStack = emptyList()) }
    }
}

data class NavState(
    val screen: Screen = Screen.Splash,
    val backStack: List<Screen> = emptyList()
) {
    val canGoBack: Boolean get() = backStack.isNotEmpty()
}
