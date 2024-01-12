package com.example.resq.presentation.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.resq.R

@Composable
fun HomeProfileSection(
    modifier: Modifier = Modifier,
    name: String,
    onRiwayatClick:() -> Unit,
    location: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = Color.Gray, shape = CircleShape),
            model = R.drawable.icon_dummy_pp,
            contentDescription = ""
        )

        Row(
            modifier = Modifier.height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hi, $name",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = location, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            ElevatedButton(
                onClick = onRiwayatClick,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                ),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = Color.Black
                ),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.History, contentDescription = "")
                    Text(text = "Riwayat")
                }
            }
        }
    }
}