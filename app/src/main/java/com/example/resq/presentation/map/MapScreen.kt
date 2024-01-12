package com.example.resq.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsNotFixed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.resq.R
import com.example.resq.databinding.MapboxViewBinding
import com.example.resq.global_component.AppDialog
import com.example.resq.global_component.AppDialogButtonOrientation
import com.example.resq.global_component.EmergencyTypeCard
import com.example.resq.global_component.CategoryCardType
import com.example.resq.global_component.NonLazyVerticalGrid
import com.example.resq.global_component.SingleContactItem
import com.example.resq.helper.EmergencyTypeIcon
import com.example.resq.helper.SnackbarHandler
import com.example.resq.mainViewModel
import com.example.resq.model.entity.FavoriteItemEntity
import com.example.resq.model.entity.FavoriteItemPhoneNumbers
import com.example.resq.global_component.FavoriteButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.location.LocationServices
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlin.system.exitProcess


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    emTypeId: String = ""
) {
    val mapView = remember {
        mutableStateOf<MapView?>(null)
    }
    val viewModel = hiltViewModel<MapViewModel>()
    val mapPermission = rememberPermissionState(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val openSettingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val openSettingUri = Uri.fromParts("package", context.packageName, null)
    openSettingIntent.data = openSettingUri
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val myLocationIcon = remember {
        mutableStateOf(Icons.Default.GpsFixed)
    }
    val isFavorite = remember {
        derivedStateOf {
            viewModel.favoriteItems.any {
                it.em_pvd_id == (viewModel.pickedEmergencyProvider.value?.em_pvd_id ?: "")
            }
        }
    }
    val onMoveListener = object : OnMoveListener {
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveBegin(detector: MoveGestureDetector) {
            myLocationIcon.value = Icons.Default.GpsNotFixed
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}

    }

    if (!mapPermission.status.isGranted) {
        if (mapPermission.status.shouldShowRationale) {
            viewModel.showPermissionWarningDialog.value = false
            viewModel.showRationaleDialog.value = true
        } else {
            viewModel.showRationaleDialog.value = false
            viewModel.showPermissionWarningDialog.value = true
        }
    } else {
        viewModel.showPermissionWarningDialog.value = false
        viewModel.showRationaleDialog.value = false
    }

    if (viewModel.showRationaleDialog.value) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Untuk mengakses halaman MAP, pastikan anda sudah mengijinkan akses lokasi dari HP anda",
            secondButton = {
                Button(
                    onClick = {
                        mapPermission.launchPermissionRequest()
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Ijinkan")
                }
            }
        )
    }

    if (viewModel.showPermissionWarningDialog.value) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Sepertinya anda harus mengijinkan akses lokasi secara manual, klik tombol di bawah ini!",
            secondButton = {
                Button(
                    onClick = {
                        context.startActivity(openSettingIntent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Buka setting")
                }
            }
        )
    }

    if (viewModel.useDummyLocation.value == null
        && !viewModel.showPermissionWarningDialog.value
        && !viewModel.showRationaleDialog.value
    ) {
        AppDialog(
            onDismissRequest = {
                navController.popBackStack()
            },
            description = "Anda bisa menggunakan lokasi realtime, namun bisa jadi obyek darurat tidak terdapat di sekitar lokasi asli anda. Sebagai prototype, anda bisa menggunakan lokasi dummy agar mendapati obyek darurat di sekitar. \n\n *NB. Pilihan ini akan selalu ditanyakan saat anda membuka halaman MAP, jadi jangan khawatir untuk mencoba semua pilihan.",
            secondButton = {
                Button(
                    onClick = {
                        viewModel.useDummyLocation.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Gunakan Lokasi Realtime")
                }
            },
            firstButton = {
                Button(
                    onClick = {
                        viewModel.useDummyLocation.value = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = "Gunakan Lokasi Dummy")
                }
            },
            appDialogButtonOrientation = AppDialogButtonOrientation.VERTICAL
        )
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    if (viewModel.pickedEmergencyProvider.value != null) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.pickedEmergencyProvider.value = null
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    FavoriteButton(
                        onClick = {
                            val favoriteItem = FavoriteItemEntity(
                                em_pvd_id = viewModel.pickedEmergencyProvider.value?.em_pvd_id
                                    ?: "",
                                em_pvd_name = viewModel.pickedEmergencyProvider.value?.name ?: "",
                                location = viewModel.pickedEmergencyProviderLocation.value?.features?.get(
                                    0
                                )?.properties?.place_formatted
                                    ?: "Error when Saved",
                                numbers = FavoriteItemPhoneNumbers(
                                    data = viewModel.emPhoneNumbers.toList()
                                )
                            )

                            if (isFavorite.value) {
                                viewModel.deleteFavoriteItem(favoriteItem)
                                viewModel.favoriteItems.removeIf {
                                    it.em_pvd_id == (viewModel.pickedEmergencyProvider.value?.em_pvd_id
                                        ?: "")
                                }
                            } else {
                                viewModel.insertNewFavoriteItem(favoriteItem)
                                viewModel.favoriteItems.add(favoriteItem)
                            }
                        },
                        isFavorite = isFavorite.value
                    )

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = viewModel.pickedEmergencyProvider.value?.name ?: "...",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                modifier = Modifier.weight(1f),
                                text = "${viewModel.availableTransportCount.value} Kendaraan Tersedia",
                                textAlign = TextAlign.End
                            )
                        }
                        Text(
                            text = viewModel
                                .pickedEmergencyProviderLocation
                                .value
                                ?.features
                                ?.get(0)
                                ?.properties?.place_formatted ?: "...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                viewModel.emPhoneNumbers.forEach { phoneNumber ->
                    SingleContactItem(
                        onCopyClicked = {
                            clipboardManager.setText(AnnotatedString(it))
                            viewModel.copiedNumber.value = it
                            SnackbarHandler.showSnackbar("Nomor telah di-copy")
                        },
                        onCallClicked = { type, number ->
                            when (type) {
                                "wa" -> {
                                    val numFix =
                                        "https://api.whatsapp.com/send?phone=${
                                            number.replace(
                                                "+",
                                                ""
                                            )
                                        }}"
                                    val callIntent = Intent(Intent.ACTION_VIEW, Uri.parse(numFix))
                                    context.startActivity(callIntent)
                                }

                                else -> {
                                    val callUri = Uri.parse("tel:$number")
                                    val callIntent = Intent(Intent.ACTION_DIAL, callUri)
                                    context.startActivity(callIntent)
                                }
                            }

                            viewModel.makeCallObjectInRealtimeDb(
                                viewModel.pickedEmergencyProvider.value?.em_pvd_id ?: "",
                                viewModel.userLong.value,
                                viewModel.userLat.value
                            )
                        },
                        enableCall = viewModel.availableTransportCount.value > 0,
                        copiedNumber = viewModel.copiedNumber.value,
                        phoneNumber = phoneNumber
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.useDummyLocation.value) {
        when (viewModel.useDummyLocation.value) {
            true -> {
                mapView.value?.let {
                    it.camera.flyTo(
                        CameraOptions
                            .Builder()
                            .center(
                                Point.fromLngLat(
                                    viewModel.userLong.value,
                                    viewModel.userLat.value
                                )
                            )
                            .zoom(15.0)
                            .build()
                    )

                    val annotationApi = it.annotations
                    val circleAnnotationManager = annotationApi.createCircleAnnotationManager()
                    val circleAnnotationOptions = CircleAnnotationOptions()
                        .withPoint(
                            Point.fromLngLat(
                                viewModel.userLong.value,
                                viewModel.userLat.value
                            )
                        )
                        .withCircleRadius(8.0)
                        .withCircleColor("#465DFF")
                    circleAnnotationManager.create(circleAnnotationOptions)
                }
            }

            false -> {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    viewModel.userLat.value = it.latitude
                    viewModel.userLong.value = it.longitude

                    mapView.value?.camera?.flyTo(
                        CameraOptions
                            .Builder()
                            .center(
                                Point.fromLngLat(
                                    viewModel.userLong.value,
                                    viewModel.userLat.value
                                )
                            )
                            .zoom(15.0)
                            .build()
                    )
                }

                mapView.value?.let {
                    it.location.updateSettings {
                        this.enabled = true
                        this.locationPuck = LocationPuck2D(
                            bearingImage = context.resources.getDrawable(
                                R.drawable.ic_circle,
                            ),
                            scaleExpression = interpolate {
                                linear()
                            }.toJson()
                        )
                    }

                    it.getMapboxMap().addOnMoveListener(onMoveListener)
                }
            }

            null -> {}
        }
    }

    LaunchedEffect(key1 = viewModel.emProviders.toList()) {
        mapView.value?.let {
            val viewAnnotationManager = it.viewAnnotationManager
            viewAnnotationManager.removeAllViewAnnotations()

            viewModel.emProviders.forEach { domain ->
                val view = viewAnnotationManager.addViewAnnotation(
                    resId = R.layout.emergency_provider_item,
                    options = viewAnnotationOptions {
                        geometry(Point.fromLngLat(domain.longitude, domain.latitude))
                    }
                )

                val compose = view.findViewById<ComposeView>(R.id.compose_item)

                compose.setContent {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = Color.White
                                ),
                                shape = CircleShape
                            )
                            .background(EmergencyTypeIcon.getContainerColor(domain.em_type))
                            .clickable {
                                viewModel.pickedEmergencyProvider.value = domain

                                viewModel.getLocationByLongLat(
                                    domain.longitude,
                                    domain.latitude
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = rememberAsyncImagePainter(
                                model = EmergencyTypeIcon.getIconId(
                                    domain.em_type
                                ) ?: R.drawable.ic_circle
                            ),
                            contentDescription = "",
                            tint = EmergencyTypeIcon.getContentColor(
                                domain.em_type
                            )
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.pickedEmTypeId.value) {
        if (viewModel.pickedEmTypeId.value.isEmpty()) {
            viewModel.getAllEmergencyProvider()
        } else {
            viewModel.getAllEmergencyProviderByTypeId(viewModel.pickedEmTypeId.value)
        }
    }

    LaunchedEffect(key1 = viewModel.pickedEmergencyProvider.value) {
        viewModel.pickedEmergencyProvider.value?.let { provider ->
            viewModel.getContactByProviderId(provider.em_pvd_id)
            viewModel.getSingleTransportCount(provider.em_pvd_id)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.pickedEmTypeId.value = emTypeId

        viewModel.getFavoriteItems()
    }

    BackHandler {
        if (mainViewModel.backClicked.value) {
            exitProcess(0)
        } else {
            SnackbarHandler.showSnackbar("Klik kembali sekali lagi untuk keluar dari OneConnect")
            mainViewModel.backClicked.value = true
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                myLocationIcon.value = Icons.Default.GpsFixed
                mapView.value?.camera?.flyTo(
                    CameraOptions
                        .Builder()
                        .center(
                            Point.fromLngLat(
                                viewModel.userLong.value,
                                viewModel.userLat.value
                            )
                        )
                        .zoom(15.0)
                        .build()
                )
            }) {
                Icon(imageVector = myLocationIcon.value, contentDescription = "")
            }
        }
    ) {
        Box(modifier = Modifier) {
            AndroidView(
                factory = {
                    val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

                    binding.root
                },
                update = {
                    mapView.value = it
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(Color.White)
            ) {
                NonLazyVerticalGrid(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    columnCount = 2,
                    containerHorizontalPadding = 8.dp
                ) {
                    viewModel.emTypes.forEach {
                        item {
                            Box(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                EmergencyTypeCard(
                                    onClick = {
                                        if (viewModel.pickedEmTypeId.value != it.emTypeId) {
                                            viewModel.pickedEmTypeId.value = it.emTypeId
                                        } else viewModel.pickedEmTypeId.value = ""
                                    },
                                    type = CategoryCardType.SMALL,
                                    id = it.emTypeId,
                                    pickedId = viewModel.pickedEmTypeId.value,
                                    word = it.word
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}