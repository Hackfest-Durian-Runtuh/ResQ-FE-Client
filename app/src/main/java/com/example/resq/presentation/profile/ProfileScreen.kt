package com.example.resq.presentation.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resq.R
import com.example.resq.helper.SnackbarHandler
import com.example.resq.mainViewModel
import com.example.resq.navhost.NavRoutes
import com.example.resq.presentation.profile.component.ProfileButtonsSection
import com.example.resq.presentation.profile.component.ProfileInformationCard
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<ProfileViewModel>()

    BackHandler {
        if (mainViewModel.backClicked.value) {
            exitProcess(0)
        } else {
            SnackbarHandler.showSnackbar("Klik kembali sekali lagi untuk keluar dari OneConnect")
            mainViewModel.backClicked.value = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Profil", style = MaterialTheme.typography.headlineSmall)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    model = R.drawable.icon_dummy_pp,
                    contentDescription = ""
                )

                Text(
                    text = viewModel.userInfo.value?.name ?: "...",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(onClick = { /*TODO*/ }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Edit Profil")
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                    }
                }
            }

            ProfileInformationCard(
                phoneNumber = viewModel.userInfo.value?.phone_number ?: "...",
                nik = viewModel.userInfo.value?.nik ?: "..."
            )

            ProfileButtonsSection(
                onKeluarClicked = {
                    viewModel.logout()
                    navController.navigate(NavRoutes.LOGIN.name) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                onBiodataClicked = {
                    navController.navigate(NavRoutes.BIODATA_FORM.name)
                }
            )
        }
    }
}