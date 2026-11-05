package com.knotcraft.tutor.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.MistBlue

@Composable
fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(ChartMid.copy(alpha = 0.72f), RoundedCornerShape(8.dp))
            .border(1.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, CircleShape)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = MistBlue
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = accent
        )
    }
}

@Composable
fun StatRow(
    cards: List<StatEntry>,
    modifier: Modifier = Modifier
) {
    val visible = cards.filter { it.visible }
    if (visible.size < 2) {
        Spacer(modifier = Modifier.height(0.dp))
        return
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        visible.forEach { entry ->
            StatCard(
                label = entry.label,
                value = entry.value,
                accent = entry.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

data class StatEntry(
    val label: String,
    val value: String,
    val accent: Color,
    val visible: Boolean
)
