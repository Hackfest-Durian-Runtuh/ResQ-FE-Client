package com.example.resq.global_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.resq.R
import com.example.resq.navhost.NavRoutes

data class BottomNavbarItem(
    val route: String,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val word: String
)

@Composable
fun BottomNavbar(
    onItemClicked: (route: String) -> Unit,
    currentRoute: String,
    selectedColor: Color,
    unselectedColor: Color
) {
    val items: List<BottomNavbarItem?> = listOf(
        BottomNavbarItem(
            route = NavRoutes.BERANDA.name,
            selectedIconId = R.drawable.navbar_home_selected,
            unselectedIconId = R.drawable.navbar_home_unselected,
            word = "Beranda"
        ),
        null,
        BottomNavbarItem(
            route = NavRoutes.PROFIL.name,
            selectedIconId = R.drawable.navbar_profil_selected,
            unselectedIconId = R.drawable.navbar_profil_unselected,
            word = "Profil"
        )
    )
    val scrWidth = LocalConfiguration.current.screenWidthDp

    BottomAppBar(
        tonalElevation = 8.dp,
        containerColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                if (item == null) {
                    Box(modifier = Modifier)
                } else {
                    Box(modifier = Modifier
                        .clickable {
                            onItemClicked(item.route)
                        }
                        .width((scrWidth / 2).dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = rememberAsyncImagePainter(model = if (currentRoute == item.route) item.selectedIconId else item.unselectedIconId),
                                contentDescription = "",
                                tint = if (currentRoute == item.route) selectedColor else unselectedColor
                            )
                            Text(
                                text = item.word,
                                color = if (currentRoute == item.route) selectedColor else unselectedColor
                            )
                        }
                    }
                }
            }
        }
    }
}