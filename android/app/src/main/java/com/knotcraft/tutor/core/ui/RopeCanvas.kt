package com.knotcraft.tutor.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.RopeShadeDeep
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed
import com.knotcraft.tutor.domain.model.CrossingMark
import com.knotcraft.tutor.domain.model.CrossingSide
import com.knotcraft.tutor.domain.model.PointF2D

@Composable
fun RopeCanvas(
    ghost: List<PointF2D>,
    traced: List<PointF2D>,
    marks: List<CrossingMark>,
    ropeColor: Color,
    accentColor: Color,
    corridorColor: Color,
    corridorAlpha: Float,
    strokeScale: Float,
    tightenScale: Float,
    finger: PointF2D?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        scale(tightenScale) {
            val ropeWidth = GameConfig.ROPE_STROKE_DP.dp.toPx() * strokeScale
            if (corridorAlpha > 0f) {
                strokePolyline(
                    ghost,
                    corridorColor.copy(alpha = corridorAlpha),
                    GameConfig.CORRIDOR_DP.dp.toPx()
                )
            }
            strokePolyline(ghost, RuleBrass, GameConfig.GHOST_STROKE_DP.dp.toPx())
            if (traced.size > 1) {
                translate(left = 2.dp.toPx(), top = 3.dp.toPx()) {
                    strokePolyline(traced, RopeShadeDeep, ropeWidth + 4.dp.toPx())
                }
                val split = (traced.size * (1f - GameConfig.WORKING_END_FRACTION)).toInt()
                    .coerceIn(1, traced.size - 1)
                strokePolyline(traced.subList(0, split + 1), ropeColor, ropeWidth)
                strokePolyline(traced.subList(split, traced.size), accentColor, ropeWidth)
            }
            marks.forEach { mark ->
                val center = Offset(mark.position.x * size.width, mark.position.y * size.height)
                if (mark.side == CrossingSide.UNDER) {
                    drawCircle(
                        color = ChipNavy,
                        radius = ropeWidth * 0.55f,
                        center = center
                    )
                }
                val ringRadius = 11.dp.toPx()
                if (mark.cleared && mark.side == CrossingSide.OVER) {
                    drawCircle(color = ropeColor, radius = ringRadius, center = center)
                } else {
                    drawCircle(
                        color = if (mark.cleared) ropeColor else Sailcloth,
                        radius = ringRadius,
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
            if (finger != null) {
                val point = Offset(finger.x * size.width, finger.y * size.height)
                drawCircle(color = Sailcloth, radius = 8.dp.toPx(), center = point)
                drawCircle(
                    color = SignalRed,
                    radius = 9.dp.toPx(),
                    center = point,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

private fun DrawScope.strokePolyline(points: List<PointF2D>, color: Color, width: Float) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points.first().x * size.width, points.first().y * size.height)
    for (index in 1 until points.size) {
        path.lineTo(points[index].x * size.width, points[index].y * size.height)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}
