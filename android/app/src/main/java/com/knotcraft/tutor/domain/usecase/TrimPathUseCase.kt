package com.knotcraft.tutor.domain.usecase

import com.knotcraft.tutor.domain.model.PointF2D

class TrimPathUseCase {

    operator fun invoke(points: List<PointF2D>, fraction: Float): List<PointF2D> {
        if (points.size < 2) return points
        val clamped = fraction.coerceIn(0f, 1f)
        if (clamped <= 0f) return emptyList()
        val last = points.size - 1
        val scaled = clamped * last
        val index = scaled.toInt().coerceIn(0, last)
        val local = scaled - index
        val head = points.subList(0, (index + 1).coerceAtMost(points.size)).toList()
        if (index >= last || local <= 0.0001f) return head
        val a = points[index]
        val b = points[index + 1]
        return head + PointF2D(a.x + (b.x - a.x) * local, a.y + (b.y - a.y) * local)
    }

    fun pointAt(points: List<PointF2D>, fraction: Float): PointF2D? {
        if (points.isEmpty()) return null
        if (points.size == 1) return points.first()
        val last = points.size - 1
        val scaled = fraction.coerceIn(0f, 1f) * last
        val index = scaled.toInt().coerceIn(0, last)
        if (index >= last) return points[last]
        val local = scaled - index
        val a = points[index]
        val b = points[index + 1]
        return PointF2D(a.x + (b.x - a.x) * local, a.y + (b.y - a.y) * local)
    }
}
