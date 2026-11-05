package com.knotcraft.tutor.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.knotcraft.tutor.domain.model.TraceResult
import com.knotcraft.tutor.presentation.app.AppViewModel
import com.knotcraft.tutor.presentation.families.FamiliesViewModel
import com.knotcraft.tutor.presentation.game.GameViewModel
import com.knotcraft.tutor.presentation.gameover.GameOverViewModel
import com.knotcraft.tutor.presentation.lesson.LessonViewModel
import com.knotcraft.tutor.presentation.menu.MenuViewModel
import com.knotcraft.tutor.presentation.quiz.QuizViewModel
import com.knotcraft.tutor.presentation.settings.SettingsViewModel
import com.knotcraft.tutor.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val knotId: String = "",
    private val result: TraceResult? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                knotRepository = ServiceLocator.knotRepository,
                progressRepository = ServiceLocator.progressRepository,
                nextKnot = ServiceLocator.nextKnot
            )

            modelClass.isAssignableFrom(FamiliesViewModel::class.java) -> FamiliesViewModel(
                knotRepository = ServiceLocator.knotRepository,
                progressRepository = ServiceLocator.progressRepository
            )

            modelClass.isAssignableFrom(LessonViewModel::class.java) -> LessonViewModel(
                knotId = knotId,
                knotRepository = ServiceLocator.knotRepository,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                buildKnotPath = ServiceLocator.buildKnotPath,
                trimPath = ServiceLocator.trimPath
            )

            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                knotId = knotId,
                knotRepository = ServiceLocator.knotRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                buildKnotPath = ServiceLocator.buildKnotPath,
                trimPath = ServiceLocator.trimPath,
                tracePath = ServiceLocator.tracePath,
                evaluateTrace = ServiceLocator.evaluateTrace
            )

            modelClass.isAssignableFrom(QuizViewModel::class.java) -> QuizViewModel(
                buildQuiz = ServiceLocator.buildQuiz,
                progressRepository = ServiceLocator.progressRepository
            )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                result = requireNotNull(result),
                progressRepository = ServiceLocator.progressRepository,
                nextKnot = ServiceLocator.nextKnot
            )

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                progressRepository = ServiceLocator.progressRepository
            )

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
