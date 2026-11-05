package com.knotcraft.tutor.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RuleBrass

@Composable
fun SectionRule(
    title: String,
    modifier: Modifier = Modifier,
    accent: Color = MistBlue
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = accent
        )
        Box(
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(RuleBrass)
        )
    }
}

@Composable
fun Hairline(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(RuleBrass)
    )
}

@Composable
fun AccentRule(
    modifier: Modifier = Modifier,
    width: Int = 72,
    accent: Color = MistBlue
) {
    Box(
        modifier = modifier
            .width(width.dp)
            .height(3.dp)
            .background(accent)
    )
}
