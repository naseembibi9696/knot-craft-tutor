package com.knotcraft.tutor.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val SCREEN_SLIDE_MS = 240
    const val TIGHTEN_MS = 500L
    const val FAIL_DELAY_MS = 500L
    const val STRAY_PULSE_MS = 150L
    const val STRAY_COOLDOWN_MS = 260L
    const val ANSWER_LOCK_MS = 900L
    const val CORRIDOR_DP = 44
    const val ROPE_STROKE_DP = 22
    const val GHOST_STROKE_DP = 20
    const val SCRUBBER_HEIGHT_DP = 56
    const val ROW_CARD_HEIGHT_DP = 72
    const val MAX_STRAYS = 3
    const val CLEAN_RUNS_TO_LEARN = 3
    const val TOTAL_KNOTS = 30
    const val QUIZ_QUESTIONS = 8
    const val LESSON_FRAME_MS = 16L
    const val LESSON_SWEEP_MS = 6000f
    const val PATH_SAMPLES = 160
    const val CORRIDOR_NORM = 0.12f
    const val TRACE_LOOKAHEAD = 26
    const val COMPLETE_THRESHOLD = 0.985f
    const val WORKING_END_FRACTION = 0.18f
}
