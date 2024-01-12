package com.example.resq.global_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

enum class AppDialogButtonOrientation{
    VERTICAL,
    HORIZONTAL
}
@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    secondButton:@Composable (() -> Unit)? = null,
    firstButton:@Composable (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
    title: String? = null,
    properties: DialogProperties? = null,
    description: String,
    appDialogButtonOrientation: AppDialogButtonOrientation = AppDialogButtonOrientation.VERTICAL
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            when(appDialogButtonOrientation){
                AppDialogButtonOrientation.VERTICAL -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        firstButton?.let {
                            it()
                        }
                        secondButton?.let {
                            it()
                        }
                    }
                }
                AppDialogButtonOrientation.HORIZONTAL -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        firstButton?.let {
                            it()
                        }
                        secondButton?.let {
                            it()
                        }
                    }
                }
            }
        },
        title = {
            title?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Text(text = description)
        },
        shape = RoundedCornerShape(8.dp),
        properties = properties ?: DialogProperties()

    )
}
