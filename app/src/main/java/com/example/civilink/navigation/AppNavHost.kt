package com.example.civilink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civilink.ui.Screens.HomeScreen.HomeScreen
import com.example.civilink.ui.Screens.Login.LoginScreen
import com.example.civilink.ui.Screens.Onboarding.Onboarding1Screen
import com.example.civilink.ui.Screens.Onboarding.Onboarding2Screen
import com.example.civilink.ui.Screens.Profile.ProfileScreen
import com.example.civilink.ui.Screens.Register.RegisterScreen
import com.example.civilink.ui.Screens.ReportDetail.ReportDetailScreen
import com.example.civilink.ui.Screens.Setting.SettingScreen
import com.example.civilink.ui.screens.admindashboard.AdminDashboardScreen
import com.example.civilink.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }
        composable(ROUT_ONBOARDING1) {
            Onboarding1Screen(navController)
        }
        composable(ROUT_ONBOARDING2) {
            Onboarding2Screen(navController)
        }
        composable(ROUT_PROFILESCREEN) {
            ProfileScreen(navController)
        }
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }
        composable(ROUT_REPORTDETAIL) {
            ReportDetailScreen(navController)
        }
        composable(ROUT_SETTINGSCREEN) {
            SettingScreen(navController)
        }
        composable(ROUT_LOGINSCREEN) {
            LoginScreen(navController)
        }
        composable(ROUT_ADMINDASHBOARD) {
            AdminDashboardScreen(navController)
        }
        composable(ROUT_HOMESCREEN) {
            HomeScreen(navController)
        }
    }
}