package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.PathTemplate
import com.knotcraft.tutor.domain.model.PointF2D
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class BuildKnotPathUseCase {

    operator fun invoke(
        template: PathTemplate,
        variation: Int,
        samples: Int,
        mirrored: Boolean
    ): List<PointF2D> {
        if (samples < 2) return emptyList()
        val seed = variation * 0.19f
        val result = ArrayList<PointF2D>(samples)
        for (index in 0 until samples) {
            val t = index.toFloat() / (samples - 1).toFloat()
            val raw = pointAt(template, t, seed)
            val x = if (mirrored) 1f - raw.x else raw.x
            result.add(PointF2D(clamp(x), clamp(raw.y)))
        }
        return result
    }

    private fun pointAt(template: PathTemplate, t: Float, seed: Float): PointF2D {
        val tau = (2.0 * PI).toFloat()
        val half = PI.toFloat()
        return when (template) {
            PathTemplate.OVERHAND_LOOP -> PointF2D(
                0.50f + 0.27f * sin(tau * t + seed),
                0.13f + 0.74f * t + 0.07f * sin(2f * tau * t)
            )
            PathTemplate.DOUBLE_LOOP -> PointF2D(
                0.50f + 0.28f * sin(2f * tau * t + seed),
                0.12f + 0.76f * t
            )
            PathTemplate.ROUND_TURN -> {
                val angle = 1.5f * tau * t + seed
                val radius = 0.30f - 0.12f * t
                PointF2D(
                    0.50f + radius * cos(angle),
                    0.30f + radius * sin(angle) + 0.34f * t
                )
            }
            PathTemplate.FIGURE_EIGHT -> PointF2D(
                0.50f + 0.28f * sin(tau * t + seed),
                0.50f + 0.33f * sin(2f * tau * t)
            )
            PathTemplate.CLOVE -> PointF2D(
                0.50f + 0.26f * sin(2f * tau * t + seed) * (1f - 0.3f * t),
                0.14f + 0.72f * t
            )
            PathTemplate.SHEET_BEND -> PointF2D(
                0.50f + 0.31f * sin(tau * t + seed) * cos(half * t * 0.8f),
                0.15f + 0.70f * t + 0.05f * sin(tau * t)
            )
            PathTemplate.CLEAT -> PointF2D(
                0.50f + 0.27f * sin(3f * tau * t + seed),
                0.15f + 0.70f * t
            )
            PathTemplate.COIL_STOPPER -> {
                val angle = 2.5f * tau * t + seed
                val radius = 0.07f + 0.23f * t
                PointF2D(
                    0.50f + radius * cos(angle),
                    0.50f + radius * sin(angle) * 0.9f
                )
            }
        }
    }

    private fun clamp(value: Float): Float = value.coerceIn(0.07f, 0.93f)
}
