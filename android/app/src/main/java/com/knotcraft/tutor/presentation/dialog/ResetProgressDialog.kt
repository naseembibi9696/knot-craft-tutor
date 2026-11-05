package com.knotcraft.tutor.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knotcraft.tutor.R
import com.knotcraft.tutor.core.theme.ChartMid
import com.knotcraft.tutor.core.theme.MistBlue
import com.knotcraft.tutor.core.theme.Sailcloth
import com.knotcraft.tutor.core.theme.SignalRed

@Composable
fun ResetProgressDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ChartMid,
        shape = RoundedCornerShape(12.dp),
        title = {
            Text(
                text = stringResource(R.string.dialog_reset_title),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = SignalRed
            )
        },
        text = {
            Text(
                text = stringResource(R.string.dialog_reset_body),
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 13.sp,
                color = Sailcloth
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.cta_wipe),
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp,
                    color = SignalRed
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
