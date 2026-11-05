package com.knotcraft.tutor.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth

@Composable
fun SecondaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Int = 48
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (enabled) RuleBrass else ChipNavy),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = ChipNavy.copy(alpha = 0.55f),
            contentColor = Sailcloth,
            disabledContainerColor = ChipNavy.copy(alpha = 0.3f),
            disabledContentColor = DeepMist
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) Sailcloth else DeepMist
        )
    }
}
