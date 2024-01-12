package com.example.resq

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.resq.global_component.BottomNavbar
import com.example.resq.global_component.LoadingLayout
import com.example.resq.navhost.AppNavHost
import com.example.resq.navhost.NavRoutes
import com.example.resq.ui.theme.OneConnectTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

lateinit var mainViewModel: MainViewModel
lateinit var _showSnackbar: (message: String) -> Unit
lateinit var _showSnackbarWithAction: (
    message: String,
    actionLabel: String,
    action: () -> Unit
) -> Unit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val _mainViewModel by viewModels<MainViewModel>()
        mainViewModel = _mainViewModel

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.e("FCM TOKEN", it)
        }

        setContent {
            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            _showSnackbar = {
                coroutineScope.launch(Dispatchers.IO) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it)
                }
            }
            _showSnackbarWithAction = { msg, label, action ->
                coroutineScope.launch(Dispatchers.IO) {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val snackbarData = snackbarHostState
                        .showSnackbar(
                            message = msg,
                            actionLabel = label
                        )

                    if (snackbarData == SnackbarResult.ActionPerformed) {
                        if (label == "Tutup") {
                            snackbarHostState.currentSnackbarData?.dismiss()
                        } else {
                            action()
                        }
                    }
                }
            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                mainViewModel.currentRoute.value = destination.route ?: NavRoutes.SPLASH.name

                when (destination.route) {
                    NavRoutes.BERANDA.name,
                    NavRoutes.MAP.name,
                    NavRoutes.PROFIL.name,
                    "${NavRoutes.MAP.name}/{em_type_id}"
                    -> mainViewModel.showBottomBar.value =
                        true

                    else -> mainViewModel.showBottomBar.value = false
                }
            }

            LaunchedEffect(key1 = mainViewModel.backClicked.value) {
                delay(2000)
                mainViewModel.backClicked.value = false
            }

            OneConnectTheme {
                val selectedNavbarItemColor = MaterialTheme.colorScheme.primary
                val unselectedNavbarItemColor = Color.Gray
                val bottomBarPadding = remember {
                    mutableStateOf(0.dp)
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = {
                                Snackbar(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .padding(bottom = bottomBarPadding.value),
                                    shape = RoundedCornerShape(16.dp),
                                    action = {
                                        it.visuals.actionLabel?.let { it1 ->
                                            Text(
                                                modifier = Modifier.clickable { it.performAction() },
                                                text = it1,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(text = it.visuals.message, color = Color.Black)
                                }
                            }
                        )
                    },
                ) {
                    bottomBarPadding.value = it.calculateBottomPadding()

                    Scaffold(
                        bottomBar = {
                            if (mainViewModel.showBottomBar.value) {
                                BottomNavbar(
                                    onItemClicked = {
                                        if (navController.currentDestination?.route != it) {
                                            navController.navigate(it)
                                        }
                                    },
                                    currentRoute = mainViewModel.currentRoute.value,
                                    selectedColor = selectedNavbarItemColor,
                                    unselectedColor = unselectedNavbarItemColor
                                )
                            }
                        },
                        floatingActionButton = {
                            val mapRoutes = listOf(
                                NavRoutes.MAP.name,
                                "${NavRoutes.MAP.name}/{em_type_id}"
                            )

                            if (mainViewModel.showBottomBar.value) {
                                Column(
                                    modifier = Modifier.offset(y = 64.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    FloatingActionButton(
                                        modifier = Modifier.border(
                                            border = BorderStroke(
                                                width = 2.dp,
                                                color = if (mainViewModel.currentRoute.value in mapRoutes) Color.White else Color.LightGray
                                            ),
                                            shape = CircleShape
                                        ),
                                        shape = CircleShape,
                                        onClick = {
                                            navController.navigate(NavRoutes.MAP.name)
                                        },
                                        containerColor = if (mainViewModel.currentRoute.value in mapRoutes) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                        contentColor = if (mainViewModel.currentRoute.value in mapRoutes) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "",
                                        )
                                    }
                                    Text(
                                        text = "Maps",
                                        color = if (mainViewModel.currentRoute.value in mapRoutes) selectedNavbarItemColor else unselectedNavbarItemColor
                                    )
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) {
                        LoadingLayout {
                            AppNavHost(
                                modifier = Modifier.padding(bottom = it.calculateBottomPadding()),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}