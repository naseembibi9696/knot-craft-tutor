package com.knotcraft.tutor.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.core.di.AppViewModelFactory
import com.knotcraft.tutor.core.navigation.Screen
import com.knotcraft.tutor.presentation.families.FamiliesScreen
import com.knotcraft.tutor.presentation.game.GameScreen
import com.knotcraft.tutor.presentation.gameover.GameOverScreen
import com.knotcraft.tutor.presentation.lesson.LessonScreen
import com.knotcraft.tutor.presentation.menu.MenuScreen
import com.knotcraft.tutor.presentation.quiz.QuizScreen
import com.knotcraft.tutor.presentation.settings.SettingsScreen
import com.knotcraft.tutor.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory())
    val nav by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = nav.canGoBack) { appViewModel.back() }

    AnimatedContent(
        targetState = nav.screen,
        modifier = Modifier.fillMaxSize(),
        transitionSpec = {
            if (initialState is Screen.Splash) {
                (fadeIn(tween(340)) + slideInVertically { it / 12 }) togetherWith
                    fadeOut(tween(300))
            } else {
                (fadeIn(tween(GameConfig.SCREEN_SLIDE_MS)) + slideInHorizontally { it / 4 }) togetherWith
                    (fadeOut(tween(GameConfig.SCREEN_SLIDE_MS)) + slideOutHorizontally { -it / 6 })
            }
        },
        label = "screenHost"
    ) { screen ->
        when (screen) {
            is Screen.Splash -> SplashScreen(
                onFinished = { appViewModel.navigateReplacing(Screen.Menu) }
            )

            is Screen.Menu -> MenuScreen(
                onStartLesson = { knotId -> appViewModel.navigate(Screen.Lesson(knotId)) },
                onFamilies = { appViewModel.navigate(Screen.Families) },
                onQuiz = { appViewModel.navigate(Screen.Quiz) },
                onSettings = { appViewModel.navigate(Screen.Settings) }
            )

            is Screen.Families -> FamiliesScreen(
                onBack = { appViewModel.back() },
                onOpenKnot = { knotId -> appViewModel.navigate(Screen.Lesson(knotId)) }
            )

            is Screen.Lesson -> LessonScreen(
                knotId = screen.knotId,
                onBack = { appViewModel.back() },
                onStartTrace = {
                    appViewModel.navigate(Screen.Game(screen.knotId, System.nanoTime()))
                }
            )

            is Screen.Game -> GameScreen(
                knotId = screen.knotId,
                runId = screen.runId,
                onBack = { appViewModel.back() },
                onShowSteps = { appViewModel.navigateReplacing(Screen.Lesson(screen.knotId)) },
                onFinished = { result ->
                    appViewModel.navigateReplacing(Screen.GameOver(result, System.nanoTime()))
                }
            )

            is Screen.Quiz -> QuizScreen(
                onBack = { appViewModel.back() },
                onFinished = { result ->
                    appViewModel.navigateReplacing(Screen.GameOver(result, System.nanoTime()))
                }
            )

            is Screen.GameOver -> GameOverScreen(
                result = screen.result,
                runId = screen.runId,
                onTraceAgain = { knotId ->
                    appViewModel.navigateReplacing(Screen.Game(knotId, System.nanoTime()))
                },
                onNextKnot = { knotId ->
                    appViewModel.navigateReplacing(Screen.Lesson(knotId))
                },
                onMenu = { appViewModel.home() }
            )

            is Screen.Settings -> SettingsScreen(
                onBack = { appViewModel.back() }
            )
        }
    }
}
