package com.knotcraft.tutor.core.di

import android.content.Context
import com.knotcraft.tutor.data.local.KnotPreferences
import com.knotcraft.tutor.data.repository.KnotRepositoryImpl
import com.knotcraft.tutor.data.repository.ProgressRepositoryImpl
import com.knotcraft.tutor.data.repository.SettingsRepositoryImpl
import com.knotcraft.tutor.domain.repository.KnotRepository
import com.knotcraft.tutor.domain.repository.ProgressRepository
import com.knotcraft.tutor.domain.repository.SettingsRepository
import com.knotcraft.tutor.domain.usecase.BuildKnotPathUseCase
import com.knotcraft.tutor.domain.usecase.BuildQuizUseCase
import com.knotcraft.tutor.domain.usecase.EvaluateTraceUseCase
import com.knotcraft.tutor.domain.usecase.GetNextKnotUseCase
import com.knotcraft.tutor.domain.usecase.TracePathUseCase
import com.knotcraft.tutor.domain.usecase.TrimPathUseCase

object ServiceLocator {

    private var preferences: KnotPreferences? = null

    fun init(context: Context) {
        if (preferences == null) {
            preferences = KnotPreferences(context.applicationContext)
        }
    }

    val knotRepository: KnotRepository by lazy { KnotRepositoryImpl() }

    val progressRepository: ProgressRepository by lazy {
        ProgressRepositoryImpl(requireNotNull(preferences))
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl(requireNotNull(preferences))
    }

    val buildKnotPath: BuildKnotPathUseCase by lazy { BuildKnotPathUseCase() }

    val trimPath: TrimPathUseCase by lazy { TrimPathUseCase() }

    val tracePath: TracePathUseCase by lazy { TracePathUseCase() }

    val evaluateTrace: EvaluateTraceUseCase by lazy { EvaluateTraceUseCase(progressRepository) }

    val nextKnot: GetNextKnotUseCase by lazy {
        GetNextKnotUseCase(knotRepository, progressRepository)
    }

    val buildQuiz: BuildQuizUseCase by lazy { BuildQuizUseCase(knotRepository) }
}
