package com.example.resq.presentation.biodata

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.resq.navhost.NavRoutes
import com.example.resq.presentation.biodata.component.PasienCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel = hiltViewModel<BiodataViewModel>()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Biodata")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "")
                    }
                }
            )
        },
        floatingActionButton = {
            ElevatedButton(
                onClick = {
                    navController.navigate("${NavRoutes.BIODATA_FORM.name}?isSaya=false")
                },
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 2.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    Text(text = "Tambah Biodata")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //TODO Uncomment this if own biodata is appears here
//            item {
//                viewModel
//                    .biodataList
//                    .filter { it.uid == it.biodata_id }
//                    .firstOrNull()
//                    ?.let { item ->
//                        PasienCard(
//                            nickname = item.nickname,
//                            name = item.fullname,
//                            onEditClick = { /*TODO*/ }
//                        )
//                    }
//            }

            items(
                viewModel
                    .biodataList
                    .filter { it.uid != it.biodata_id },
                key = { it.biodata_id }
            ) { item ->
                PasienCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    nickname = item.nickname,
                    name = item.fullname,
                    onEditClick = {
                        navController.navigate("${NavRoutes.BIODATA_FORM.name}?isSaya=false/${item.biodata_id}")
                    }
                )
            }
        }
    }
}