package com.example.resq.global_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteButton(
    onClick: () -> Unit,
    isFavorite:Boolean
) {
    ElevatedButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if(isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if(isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if(isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = ""
            )
            Text(text = "Favorit")
        }
    }
}