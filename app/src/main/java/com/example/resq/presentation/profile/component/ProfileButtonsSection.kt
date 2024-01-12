package com.example.resq.presentation.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.resq.R

@Composable
fun ProfileButtonsSection(
    onKeluarClicked: () -> Unit,
    onBiodataClicked: () -> Unit
) {
    val listOfClick = listOf(
        onBiodataClicked,
        {},
        {},
        onKeluarClicked
    )
    val listOfWord = listOf(
        "Biodata","Hubungi Kami", "Kebijakan Privasi", "Keluar"
    )
    val listOfIcon = listOf(
        R.drawable.icon_biodata,
        R.drawable.icon_calling,
        R.drawable.icon_kebijakan_privasi,
        R.drawable.icon_logout
    )

    Column {
        listOfWord.forEachIndexed { index, s ->
            Column(
                modifier = Modifier.clickable {
                    listOfClick[index]()
                }
            ) {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = rememberAsyncImagePainter(model = listOfIcon[index]),
                        contentDescription = null,
                        tint = if (s == "Keluar") MaterialTheme.colorScheme.error else Color.Black
                    )

                    Text(
                        text = s,
                        color = if (s == "Keluar") MaterialTheme.colorScheme.error else Color.Black
                    )
                }
            }
        }
    }
}