package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.PointF2D
import kotlin.math.sqrt

class TracePathUseCase {

    data class Projection(val index: Int, val fraction: Float, val distance: Float)

    fun project(
        points: List<PointF2D>,
        target: PointF2D,
        aspect: Float,
        fromIndex: Int,
        lookAhead: Int
    ): Projection {
        if (points.size < 2) return Projection(0, 0f, Float.MAX_VALUE)
        val last = points.size - 1
        val start = fromIndex.coerceIn(0, last - 1)
        val end = (start + lookAhead).coerceIn(start + 1, last)
        var bestDistance = Float.MAX_VALUE
        var bestIndex = start
        var bestLocal = 0f
        for (i in start until end) {
            val a = points[i]
            val b = points[i + 1]
            val abx = b.x - a.x
            val aby = (b.y - a.y) * aspect
            val apx = target.x - a.x
            val apy = (target.y - a.y) * aspect
            val lengthSquared = abx * abx + aby * aby
            val u = if (lengthSquared <= 0.000001f) {
                0f
            } else {
                ((apx * abx + apy * aby) / lengthSquared).coerceIn(0f, 1f)
            }
            val dx = apx - abx * u
            val dy = apy - aby * u
            val distance = sqrt(dx * dx + dy * dy)
            if (distance < bestDistance) {
                bestDistance = distance
                bestIndex = i
                bestLocal = u
            }
        }
        val fraction = (bestIndex + bestLocal) / last.toFloat()
        return Projection(bestIndex, fraction.coerceIn(0f, 1f), bestDistance)
    }
}
