package com.knotcraft.tutor.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotcraft.tutor.core.config.GameConfig
import com.knotcraft.tutor.core.theme.ChartBase
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.RuleBrass
import com.knotcraft.tutor.core.theme.Sailcloth

@Composable
fun KnotRowCard(
    name: String,
    caption: String,
    streak: Int,
    learned: Boolean,
    learnedLabel: String,
    thumb: Int,
    thumbDescription: String,
    stateLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(GameConfig.ROW_CARD_HEIGHT_DP.dp)
                .background(ChartMid.copy(alpha = 0.55f), RoundedCornerShape(6.dp))
                .clickable(onClick = onClick)
                .semantics { stateDescription = stateLabel }
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(ChipNavy, RoundedCornerShape(6.dp))
                    .border(1.dp, RuleBrass, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(thumb),
                    contentDescription = thumbDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 17.sp,
                    color = Sailcloth,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = caption,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = DeepMist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(GameConfig.CLEAN_RUNS_TO_LEARN) { index ->
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    if (index < streak) RopeGold else RuleBrass,
                                    CircleShape
                                )
                        )
                    }
                }
                if (learned) {
                    Text(
                        text = learnedLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChartBase,
                        modifier = Modifier
                            .background(RopeGold, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
        Hairline(modifier = Modifier.padding(top = 6.dp))
    }
}
