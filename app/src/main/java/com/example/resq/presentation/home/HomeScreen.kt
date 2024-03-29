package com.example.resq.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resq.R
import com.example.resq.global_component.EmergencyTypeCard
import com.example.resq.global_component.CategoryCardType
import com.example.resq.global_component.ContactInfoCard
import com.example.resq.global_component.LastCallCard
import com.example.resq.global_component.NonLazyVerticalGrid
import com.example.resq.helper.SnackbarHandler
import com.example.resq.mainViewModel
import com.example.resq.model.domain.general.PhoneNumberDomain
import com.example.resq.navhost.NavRoutes
import com.example.resq.presentation.home.component.HomeProfileSection
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.getAllFavorites()
    }

    LaunchedEffect(key1 = viewModel.favoritePhoneProviders.toList()) {
        if (viewModel.favoritePhoneProviders.isNotEmpty()) {
            viewModel.getMultipleTransportCount(
                viewModel.favoritePhoneProviders.toList().map { it.em_pvd_id }
            )
        }
    }

    LaunchedEffect(key1 = viewModel.lastCall.value) {
        viewModel.lastCall.value?.let {
            viewModel.getEmergencyProviderById(it.em_pvd_id ?: "")
        }
    }

    LaunchedEffect(key1 = viewModel.lastCallEmProvider.value) {
        viewModel.lastCallEmProvider.value?.let {
            viewModel.getLocationFromLongLat(
                long = it.longitude?.toDouble() ?: .0,
                lat = it.latitude?.toDouble() ?: .0
            )
        }
    }

    BackHandler {
        if (mainViewModel.backClicked.value) {
            exitProcess(0)
        } else {
            SnackbarHandler.showSnackbar("Klik kembali sekali lagi untuk keluar dari OneConnect")
            mainViewModel.backClicked.value = true
        }
    }

    if (viewModel.calledContactNumber.value != null && viewModel.showPasienSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.calledContactNumber.value = null
                viewModel.showPasienSheet.value = false
            },
            containerColor = Color.White
        ) {
            NonLazyVerticalGrid(
                columnCount = 2,
                containerHorizontalPadding = 6.dp
            ) {
                item {
                    viewModel
                        .pasienList
                        .filter { it.uid == it.biodata_id }
                        .firstOrNull()
                        ?.let { item ->
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                onClick = {
//                                    viewModel.makeCallObjectInRealtimeDb(
//                                        biodata_id = item.biodata_id,
//                                        viewModel.pickedEmergencyProvider.value?.em_pvd_id ?: "",
//                                        viewModel.userLong.value,
//                                        viewModel.userLat.value
//                                    )

                                    viewModel.calledContactNumber.value?.let { contact ->
                                        when (contact.contactType) {
                                            "wa" -> {
                                                val numFix =
                                                    "https://api.whatsapp.com/send?phone=${
                                                        contact.phoneNumber.replace(
                                                            "+",
                                                            ""
                                                        )
                                                    }}"
                                                val callIntent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(numFix))
                                                context.startActivity(callIntent)
                                            }

                                            else -> {
                                                val callUri =
                                                    Uri.parse("tel:${contact.phoneNumber}")
                                                val callIntent = Intent(Intent.ACTION_DIAL, callUri)
                                                context.startActivity(callIntent)
                                            }
                                        }
                                    }
                                },
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.size(60.dp),
                                            model = R.drawable.icon_dummy_pp,
                                            contentDescription = ""
                                        )
                                    }
                                    Text(text = "Saya")
                                }
                            }
                        }
                }

                viewModel
                    .pasienList
                    .filter { it.uid != it.biodata_id }
                    .forEach { item ->
                        item {
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                onClick = {
//                                    viewModel.makeCallObjectInRealtimeDb(
//                                        biodata_id = item.biodata_id,
//                                        viewModel.pickedEmergencyProvider.value?.em_pvd_id ?: "",
//                                        viewModel.userLong.value,
//                                        viewModel.userLat.value
//                                    )

                                    viewModel.calledContactNumber.value?.let { contact ->
                                        when (contact.contactType) {
                                            "wa" -> {
                                                val numFix =
                                                    "https://api.whatsapp.com/send?phone=${
                                                        contact.phoneNumber.replace(
                                                            "+",
                                                            ""
                                                        )
                                                    }}"
                                                val callIntent =
                                                    Intent(Intent.ACTION_VIEW, Uri.parse(numFix))
                                                context.startActivity(callIntent)
                                            }

                                            else -> {
                                                val callUri =
                                                    Uri.parse("tel:${contact.phoneNumber}")
                                                val callIntent = Intent(Intent.ACTION_DIAL, callUri)
                                                context.startActivity(callIntent)
                                            }
                                        }
                                    }
                                },
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            modifier = Modifier.size(60.dp),
                                            model = R.drawable.icon_dummy_pp,
                                            contentDescription = ""
                                        )
                                    }
                                    Text(text = item.nickname)
                                }
                            }
                        }
                    }
            }

            Button(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                onClick = {
                    viewModel.calledContactNumber.value = null
                    viewModel.showPasienSheet.value = false
                }
            ) {
                Text(text = "Tutup")
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeProfileSection(
                modifier = Modifier.padding(top = 16.dp),
                name = viewModel.userInfo.value?.fullname ?: "...",
                location = "Jawa Timur, Indonesia",
                onRiwayatClick = {
                    navController.navigate(
                        route = NavRoutes.HISTORY.name
                    )
                }
            )
        }
        viewModel.lastCall.value?.let {
            item {
                Text(text = "Panggilan Terakhir", style = MaterialTheme.typography.headlineSmall)
            }

            item {
                LastCallCard(
                    onLihatDetailClick = {
                        navController.navigate(
                            route = "${NavRoutes.CALL_DETAIL.name}/${viewModel.lastCall.value?.em_call_id ?: ""}"
                        )
                    },
                    name = viewModel.lastCallEmProvider.value?.name ?: "",
                    locationOrDate = viewModel.lastCallLocationResponse.value?.features?.get(0)?.properties?.place_formatted
                        ?: "",
                    status = viewModel.callStatusMap[viewModel.lastCall.value?.em_call_status_id
                        ?: ""] ?: "..."
                )
            }
        }
        item {
            Text(text = "Layanan Darurat", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            val scrWidth = LocalConfiguration.current.screenWidthDp
            NonLazyVerticalGrid(
                modifier = Modifier,
                containerWidth = (scrWidth - 32).dp,
                columnCount = 2,
                verticalSpacing = 4.dp
            ) {
                viewModel.emTypes.forEach {
                    item {
                        EmergencyTypeCard(
                            modifier = Modifier.padding(4.dp),
                            type = CategoryCardType.BIG,
                            id = it.emTypeId,
                            word = it.word,
                            onClick = {
                                navController.navigate(
                                    "${NavRoutes.MAP.name}/${it.emTypeId}"
                                )
                            }
                        )
                    }
                }
            }
        }
        item {
            Text(text = "Nomor Favorit", style = MaterialTheme.typography.headlineSmall)
        }
        if (viewModel.favoritePhoneProviders.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Belum Ada Nomor Favorit",
                    textAlign = TextAlign.Center
                )
            }
        }
        items(viewModel.favoritePhoneProviders) { item ->
            ContactInfoCard(
                location = item.location ?: "",
                name = item.em_pvd_name ?: "",
                phoneNumber = item.numbers.data,
                onCallClicked = { type, number ->
                    viewModel.calledContactNumber.value = PhoneNumberDomain(
                        contactType = type,
                        phoneNumber = number
                    )

                    viewModel.showPasienSheet.value = true
                },
                onCopyClicked = {
                    clipboardManager.setText(AnnotatedString(it))
                    viewModel.copiedNumber.value = it
                    SnackbarHandler.showSnackbar("Nomor telah di-copy")
                },
                copiedNumber = viewModel.copiedNumber.value,
                onDeleteClick = {
                    viewModel.deleteFavoriteItem(item)
                    viewModel.favoritePhoneProviders.removeIf {
                        it.em_pvd_id == item.em_pvd_id
                    }
                    SnackbarHandler.showSnackbar("Berhasil dihapus dari favorit")
                },
                availableTransportCount = (viewModel.availableTransportCountMaps.value[item.em_pvd_id]
                    ?: 0)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}