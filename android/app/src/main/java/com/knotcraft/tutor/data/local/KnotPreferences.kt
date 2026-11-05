package com.knotcraft.tutor.data.local

import android.content.Context
import android.content.SharedPreferences

class KnotPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun streakOf(knotId: String): Int = prefs.getInt(KEY_STREAK + knotId, 0)

    fun attemptsOf(knotId: String): Int = prefs.getInt(KEY_ATTEMPTS + knotId, 0)

    fun bestAccuracyOf(knotId: String): Int = prefs.getInt(KEY_ACCURACY + knotId, 0)

    fun isLearned(knotId: String): Boolean = learnedIds().contains(knotId)

    fun learnedIds(): Set<String> =
        prefs.getStringSet(KEY_LEARNED, emptySet())?.toSet() ?: emptySet()

    fun writeProgress(knotId: String, streak: Int, attempts: Int, accuracy: Int, learned: Boolean) {
        val editor = prefs.edit()
        editor.putInt(KEY_STREAK + knotId, streak)
        editor.putInt(KEY_ATTEMPTS + knotId, attempts)
        editor.putInt(KEY_ACCURACY + knotId, accuracy)
        if (learned) {
            editor.putStringSet(KEY_LEARNED, learnedIds() + knotId)
        }
        editor.apply()
    }

    fun recentIds(): List<String> {
        val raw = prefs.getString(KEY_RECENT, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        return raw.split(SEPARATOR).filter { it.isNotBlank() }
    }

    fun pushRecent(knotId: String, limit: Int) {
        val updated = (listOf(knotId) + recentIds().filter { it != knotId }).take(limit)
        prefs.edit().putString(KEY_RECENT, updated.joinToString(SEPARATOR)).apply()
    }

    fun quizBest(): Int = prefs.getInt(KEY_QUIZ_BEST, 0)

    fun writeQuizBest(score: Int) {
        prefs.edit().putInt(KEY_QUIZ_BEST, score).apply()
    }

    fun mirrored(): Boolean = prefs.getBoolean(KEY_MIRROR, false)

    fun writeMirrored(value: Boolean) {
        prefs.edit().putBoolean(KEY_MIRROR, value).apply()
    }

    fun ropeShadeIndex(): Int = prefs.getInt(KEY_SHADE, 0)

    fun writeRopeShadeIndex(value: Int) {
        prefs.edit().putInt(KEY_SHADE, value).apply()
    }

    fun speedIndex(): Int = prefs.getInt(KEY_SPEED, 1)

    fun writeSpeedIndex(value: Int) {
        prefs.edit().putInt(KEY_SPEED, value).apply()
    }

    fun haptics(): Boolean = prefs.getBoolean(KEY_HAPTICS, true)

    fun writeHaptics(value: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS, value).apply()
    }

    fun clearProgress() {
        val mirror = mirrored()
        val shade = ropeShadeIndex()
        val speed = speedIndex()
        val haptics = haptics()
        prefs.edit().clear().apply()
        writeMirrored(mirror)
        writeRopeShadeIndex(shade)
        writeSpeedIndex(speed)
        writeHaptics(haptics)
    }

    private companion object {
        const val FILE_NAME = "knotcraft_prefs"
        const val KEY_STREAK = "streak_"
        const val KEY_ATTEMPTS = "attempts_"
        const val KEY_ACCURACY = "accuracy_"
        const val KEY_LEARNED = "learned_ids"
        const val KEY_RECENT = "recent_ids"
        const val KEY_QUIZ_BEST = "quiz_best"
        const val KEY_MIRROR = "mirror_enabled"
        const val KEY_SHADE = "rope_shade"
        const val KEY_SPEED = "speed_index"
        const val KEY_HAPTICS = "haptics_enabled"
        const val SEPARATOR = ","
    }
}
