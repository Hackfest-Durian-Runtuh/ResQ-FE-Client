package com.example.resq.presentation.history

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.resq.global_component.LastCallCard
import com.example.resq.navhost.NavRoutes
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@Composable
fun HistoryScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<HistoryViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Riwayat")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(viewModel.listCall) { item ->
                val second = (item.created_at?.get("value") ?: "0").toLong()
                val formatter = SimpleDateFormat("EEEE, d-MM-yyyy HH:mm")
                val dateString: String = formatter.format(Date(second))

                LastCallCard(
                    onLihatDetailClick = {
                        navController.navigate(
                            route = "${NavRoutes.CALL_DETAIL.name}/${item.em_call_id ?: ""}"
                        )
                    },
                    name = viewModel.providerNameMap[item.em_pvd_id ?: ""]?.name ?: "...",
                    locationOrDate = dateString,
                    status = viewModel.statusMap[item.em_call_status_id ?: ""] ?: "..."
                )
            }
        }
    }
}