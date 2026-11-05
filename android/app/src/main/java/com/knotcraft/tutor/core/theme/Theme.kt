package com.knotcraft.tutor.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val KnotColorScheme = darkColorScheme(
    primary = RopeGold,
    onPrimary = ChartBase,
    primaryContainer = RopeShadeDeep,
    onPrimaryContainer = Sailcloth,
    secondary = SignalRed,
    onSecondary = Sailcloth,
    secondaryContainer = ChipNavy,
    onSecondaryContainer = MistBlue,
    tertiary = RopeLight,
    onTertiary = ChartBase,
    background = ChartBase,
    onBackground = Sailcloth,
    surface = CardNavy,
    onSurface = Sailcloth,
    surfaceVariant = ChipNavy,
    onSurfaceVariant = MistBlue,
    surfaceContainer = ChartMid,
    outline = RuleBrass,
    outlineVariant = ChipNavy,
    error = SignalRed,
    onError = Sailcloth,
    scrim = ChartDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KnotColorScheme,
        typography = KnotTypography,
        content = content
    )
}
