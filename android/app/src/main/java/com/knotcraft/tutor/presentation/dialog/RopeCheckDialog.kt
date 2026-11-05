package com.knotcraft.tutor.presentation.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.ChipNavy
import com.knotcraft.tutor.core.theme.DeepMist
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.RopeGold
import com.knotcraft.tutor.core.theme.Sailcloth

@Composable
fun RopeCheckDialog(
    checks: List<Boolean>,
    allChecked: Boolean,
    onToggle: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val labels = listOf(
        stringResource(R.string.check_rope_in_hand),
        stringResource(R.string.check_shape_matches),
        stringResource(R.string.check_order),
        stringResource(R.string.check_holds)
    )
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ChartMid,
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(
                text = stringResource(R.string.dialog_rope_check),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = RopeGold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.dialog_rope_check_intro),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = DeepMist
                )
                Spacer(modifier = Modifier.heightIn(min = 8.dp))
                labels.forEachIndexed { index, label ->
                    val checked = checks.getOrElse(index) { false }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .clickable { onToggle(index) }
                            .semantics { stateDescription = if (checked) onLabel else offLabel },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { onToggle(index) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = RopeGold,
                                uncheckedColor = MistBlue,
                                checkmarkColor = ChipNavy
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp,
                            color = Sailcloth
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = allChecked) {
                Text(
                    text = stringResource(R.string.cta_rope_checked),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp,
                    color = if (allChecked) RopeGold else DeepMist
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_cancel),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp,
                    color = MistBlue
                )
            }
        }
    )
}
