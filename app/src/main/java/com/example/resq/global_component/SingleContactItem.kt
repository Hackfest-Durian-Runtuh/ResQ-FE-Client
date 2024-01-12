package com.example.resq.global_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.resq.R
import com.example.resq.helper.ContactTypeIcon
import com.example.resq.model.domain.general.PhoneNumberDomain

@Composable
fun SingleContactItem(
    onCopyClicked: (String) -> Unit,
    onCallClicked: (type: String, number: String) -> Unit,
    enableCall:Boolean = true,
    copiedNumber: String,
    phoneNumber: PhoneNumberDomain
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                painter = rememberAsyncImagePainter(
                    model = ContactTypeIcon.getIconId(phoneNumber.contactType)
                        ?: R.drawable.ic_circle
                ),
                contentDescription = "",
                tint = Color.Unspecified
            )
            Text(
                text = phoneNumber.phoneNumber,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.titleSmall
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Button(
                onClick = { onCopyClicked(phoneNumber.phoneNumber) },
                shape = RoundedCornerShape(Int.MAX_VALUE.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (copiedNumber == phoneNumber.phoneNumber) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (copiedNumber == phoneNumber.phoneNumber) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    painter = rememberAsyncImagePainter(model = if (copiedNumber == phoneNumber.phoneNumber) R.drawable.icon_copied else R.drawable.icon_copy),
                    contentDescription = ""
                )
            }

            Button(
                onClick = {
                    onCallClicked(
                        phoneNumber.contactType,
                        phoneNumber.phoneNumber
                    )
                },
                shape = RoundedCornerShape(Int.MAX_VALUE.dp),
                enabled = enableCall
            ) {
                Icon(
                    painter = rememberAsyncImagePainter(model = R.drawable.icon_call),
                    contentDescription = ""
                )
            }
        }
    }
}