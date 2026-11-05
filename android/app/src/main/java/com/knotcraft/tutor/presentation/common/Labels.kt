package com.knotcraft.tutor.presentation.common

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.domain.model.KnotFamily
import com.knotcraft.tutor.domain.model.RopeShade

@StringRes
fun familyLabelRes(family: KnotFamily): Int = when (family) {
    KnotFamily.LOOPS -> R.string.family_loops
    KnotFamily.HITCHES -> R.string.family_hitches
    KnotFamily.BENDS -> R.string.family_bends
    KnotFamily.STOPPERS -> R.string.family_stoppers
}

fun thumbFor(slot: Int): Int = when (slot) {
    0 -> R.drawable.sprite_3
    1 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}

fun quizSpriteFor(slot: Int): Int = when (slot) {
    0 -> R.drawable.sprite_2
    1 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}

fun ropeColorOf(shade: RopeShade): Color = when (shade) {
    RopeShade.MANILA -> RopeGold
    RopeShade.SIGNAL -> SignalRed
    RopeShade.SAILCLOTH -> Sailcloth
}

fun difficultyMark(level: Int): String = when {
    level <= 1 -> "·"
    level == 2 -> "· ·"
    else -> "· · ·"
}
