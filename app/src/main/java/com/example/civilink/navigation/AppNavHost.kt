package com.example.civilink.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.civilink.ui.screens.Createreport.CreateReportScreen
import com.example.civilink.ui.screens.homescreen.HomeScreen
import com.example.civilink.ui.screens.login.LoginScreen
import com.example.civilink.ui.screens.onboarding.Onboarding1Screen
import com.example.civilink.ui.screens.onboarding.Onboarding2Screen
import com.example.civilink.ui.screens.profile.ProfileScreen
import com.example.civilink.ui.screens.register.RegisterScreen
import com.example.civilink.ui.screens.reportdetail.ReportDetailScreen
import com.example.civilink.ui.screens.setting.SettingScreen
import com.example.civilink.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH,
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        // Splash Screen
        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

        // Onboarding Screen 1
        composable(ROUT_ONBOARDING1) {
            Onboarding1Screen(navController)
        }

        // Onboarding Screen 2
        composable(ROUT_ONBOARDING2) {
            Onboarding2Screen(navController)
        }

        // Profile Screen
        composable(ROUT_PROFILESCREEN) {
            ProfileScreen(navController)
        }

        // Register Screen
        composable(ROUT_REGISTER) {
            RegisterScreen(navController)
        }

        // Report Detail Screen
        composable(ROUT_REPORTDETAIL) {
            ReportDetailScreen(navController)
        }

        // Settings Screen
        composable(ROUT_SETTINGSCREEN) {
            SettingScreen(navController)
        }

        // Login Screen
        composable(ROUT_LOGINSCREEN) {
            LoginScreen(navController)
        }

        // Home Screen
        composable(ROUT_HOMESCREEN) {
            HomeScreen(navController)
        }
        composable(ROUT_CREATEREPORT) {
            CreateReportScreen(navController)
        }
    }
}