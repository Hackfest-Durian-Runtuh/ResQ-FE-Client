package com.example.resq.navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.resq.presentation.biodata.BiodataScreen
import com.example.resq.presentation.biodata_form.BiodataFormScreen
import com.example.resq.presentation.call_detail.CallDetailScreen
import com.example.resq.presentation.history.HistoryScreen
import com.example.resq.presentation.home.HomeScreen
import com.example.resq.presentation.login.LoginScreen
import com.example.resq.presentation.map.MapScreen
import com.example.resq.presentation.otp.OtpScreen
import com.example.resq.presentation.profile.ProfileScreen
import com.example.resq.presentation.splash.SplashScreen
import com.example.resq.presentation.user_data_input.UserDataInputScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavRoutes.SPLASH.name
    ) {
        composable(NavRoutes.SPLASH.name) {
            SplashScreen(navController = navController)
        }

        composable(NavRoutes.BERANDA.name) {
            HomeScreen(navController = navController)
        }

        composable(NavRoutes.MAP.name) {
            MapScreen(navController = navController)
        }

        composable(NavRoutes.PROFIL.name) {
            ProfileScreen(navController = navController)
        }

        composable(NavRoutes.LOGIN.name) {
            LoginScreen(navController = navController)
        }

        composable(
            route = "${NavRoutes.OTP.name}/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                }
            )
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber") ?: ""

            OtpScreen(
                phoneNumber = phoneNumber,
                navController = navController
            )
        }

        composable(
            route = "${NavRoutes.USER_DATA_INPUT.name}/{phoneNumber}",
            arguments = listOf(
                navArgument("phoneNumber") {
                    type = NavType.StringType
                }
            )
        ) {
            val phoneNumber = it.arguments?.getString("phoneNumber") ?: ""

            UserDataInputScreen(phoneNumber = phoneNumber, navController = navController)
        }

        composable(
            route = "${NavRoutes.MAP.name}/{em_type_id}",
            arguments = listOf(
                navArgument("em_type_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val emTypeId = it.arguments?.getString("em_type_id") ?: ""

            MapScreen(
                navController = navController,
                emTypeId = emTypeId
            )
        }

        composable(
            route = "${NavRoutes.CALL_DETAIL.name}/{em_call_id}",
            arguments = listOf(
                navArgument("em_call_id") {
                    type = NavType.StringType
                }
            )
        ) {
            val emCallId = it.arguments?.getString("em_call_id") ?: ""

            CallDetailScreen(
                navController = navController,
                emCallId = emCallId
            )
        }

        composable(
            route = NavRoutes.HISTORY.name
        ){
            HistoryScreen(navController = navController)
        }

        composable(
            route = NavRoutes.BIODATA.name
        ){
            BiodataScreen(navController = navController)
        }

        composable(
            route = NavRoutes.BIODATA_FORM.name
        ){
            // BIODATA FORM BARU
            BiodataFormScreen(navController = navController)
        }

        composable(
            route = "${NavRoutes.BIODATA_FORM.name}/{id}",
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                }
            )
        ){
            // BIODATA FORM LAMA
            val id = it.arguments?.getString("id") ?: ""

            BiodataFormScreen(
                id = id,
                isNewData = false,
                navController = navController
            )
        }
    }
}