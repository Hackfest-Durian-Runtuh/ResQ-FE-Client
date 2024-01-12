package com.example.resq.presentation.call_detail

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.resq.R
import com.example.resq.databinding.MapboxViewBinding
import com.example.resq.helper.CallStatus
import com.example.resq.helper.EmergencyTypeIcon
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.expressions.dsl.generated.id
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createCircleAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CallDetailScreen(
    navController: NavController,
    emCallId: String
) {
    val viewModel = hiltViewModel<CallDetailViewModel>()
    val mapView = remember {
        mutableStateOf<MapView?>(null)
    }

    LaunchedEffect(key1 = true) {
        viewModel.getCallInfoFromId(
            id = emCallId,
            onListened = { call ->
                if (viewModel.call.value == null) {
                    viewModel.call.value = call
                }

                viewModel.realtimeStatus.value = viewModel.statusMap[call.em_call_status_id
                    ?: ""] ?: "..."

                call.transport_lat?.let {
                    viewModel.lat.value = it.toDoubleOrNull() ?: .0
                }

                call.transport_long?.let {
                    viewModel.long.value = it.toDoubleOrNull() ?: .0
                }
            },
            onFailed = {
                Log.e("ERROR", it.toString())
            }
        )
    }

    LaunchedEffect(key1 = viewModel.emProvider.value) {
        viewModel.emProvider.value?.let { provider ->
            viewModel.call.value?.let { call ->
                mapView.value?.let { map ->
                    val viewAnnotationManager = map.viewAnnotationManager
                    viewAnnotationManager.removeAllViewAnnotations()

                    if (call.em_call_status_id == CallStatus.DIPROSES) {
                        viewAnnotationManager.removeAllViewAnnotations()
                    } else {
                        val view = viewAnnotationManager.addViewAnnotation(
                            resId = R.layout.emergency_provider_item,
                            options = viewAnnotationOptions {
                                geometry(
                                    Point.fromLngLat(
                                        provider
                                            .longitude
                                            ?.toDouble() ?: .0,
                                        provider
                                            .latitude
                                            ?.toDouble() ?: .0
                                    )
                                )
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
                                    .background(
                                        EmergencyTypeIcon.getContainerColor(
                                            provider.em_type ?: ""
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = rememberAsyncImagePainter(
                                        model = EmergencyTypeIcon.getIconId(
                                            provider.em_type ?: ""
                                        ) ?: R.drawable.ic_circle
                                    ),
                                    contentDescription = "",
                                    tint = EmergencyTypeIcon.getContentColor(
                                        provider.em_type ?: ""
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.call.value) {
        viewModel.call.value?.let { call ->
            viewModel.getEmProvider(
                emPvdId = call.em_pvd_id ?: ""
            )

            mapView.value?.let {
                it.camera.flyTo(
                    cameraOptions = CameraOptions
                        .Builder()
                        .center(
                            Point.fromLngLat(
                                call.user_long?.toDouble() ?: .0,
                                call.user_lat?.toDouble() ?: .0
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
                            call.user_long?.toDouble() ?: .0,
                            call.user_lat?.toDouble() ?: .0
                        )
                    )
                    .withCircleRadius(8.0)
                    .withCircleColor("#465DFF")
                circleAnnotationManager.deleteAll()
                circleAnnotationManager.create(circleAnnotationOptions)
            }
        }
    }

    LaunchedEffect(key1 = viewModel.long.value, key2 = viewModel.lat.value){
        mapView.value?.let { map ->
            if(viewModel.long.value > 0 && viewModel.lat.value > 0){
                val viewAnnotationManager = map.viewAnnotationManager
                viewAnnotationManager.annotations

                viewAnnotationManager.removeAllViewAnnotations()

                val view = viewAnnotationManager.addViewAnnotation(
                    resId = R.layout.emergency_provider_item,
                    options = viewAnnotationOptions {
                        geometry(
                            Point.fromLngLat(
                                viewModel.long.value,
                                viewModel.lat.value
                            )
                        )
                    }
                )
                val compose = view.findViewById<ComposeView>(R.id.compose_item)
                compose.setContent {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = Color.White
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.ic_circle
                            ),
                            contentDescription = "",
                            tint = Color.Green
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = mapView.value){
        mapView.value?.let { map ->
            //TODO
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detail Status") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")
                    }
                })
        }
    ) {
        Box(modifier = Modifier, contentAlignment = Alignment.BottomCenter) {
            AndroidView(
                factory = {
                    val binding = MapboxViewBinding.inflate(LayoutInflater.from(it))

                    binding.root
                },
                update = {
                    mapView.value = it
                }
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = viewModel.emProvider.value?.name ?: "...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            overflow = TextOverflow.Clip
                        )

                        Text(
                            text = viewModel.emProviderTypeMap[viewModel.emProvider.value?.em_type
                                ?: ""] ?: "...",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                            overflow = TextOverflow.Clip
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = viewModel.realtimeStatus.value,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }
    }
}