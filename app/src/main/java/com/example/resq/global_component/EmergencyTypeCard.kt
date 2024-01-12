package com.example.resq.global_component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.resq.helper.EmergencyTypeIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyTypeCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    pickedId:String = "",
    id: String,
    type: CategoryCardType,
    word: String
) {
    OutlinedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = onClick,
        border = BorderStroke(
            width = 3.dp,
            color = if(pickedId == id) Color.Gray else Color.Unspecified
        )
    ) {
        when (type) {
            CategoryCardType.BIG -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(72.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmergencyTypeIcon.getContainerColor(id)),
                        contentAlignment = Alignment.Center
                    ) {
                        EmergencyTypeIcon.getIconId(id)?.let { iconId ->
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = rememberAsyncImagePainter(model = iconId),
                                contentDescription = "",
                                tint = EmergencyTypeIcon.getContentColor(id)
                            )
                        }
                    }
                    Text(text = word, style = MaterialTheme.typography.titleLarge)
                }
            }

            CategoryCardType.SMALL -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(EmergencyTypeIcon.getContainerColor(id)),
                        contentAlignment = Alignment.Center
                    ) {
                        EmergencyTypeIcon.getIconId(id)?.let { iconId ->
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = rememberAsyncImagePainter(model = iconId),
                                contentDescription = "",
                                tint = EmergencyTypeIcon.getContentColor(id)
                            )
                        }
                    }
                    Text(text = word, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

enum class CategoryCardType {
    SMALL,
    BIG
}