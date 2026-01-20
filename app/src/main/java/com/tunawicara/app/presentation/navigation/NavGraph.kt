package com.tunawicara.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tunawicara.app.presentation.auth.CompleteProfileScreen
import com.tunawicara.app.presentation.auth.ForgotPasswordScreen
import com.tunawicara.app.presentation.auth.LoginScreen
import com.tunawicara.app.presentation.auth.SignupScreen
import com.tunawicara.app.presentation.revoice.ReVoiceScreen

/**
 * Navigation graph for the app
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Screens
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Beranda.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNeedsProfileCompletion = {
                    navController.navigate(Screen.CompleteProfile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Signup.route) {
            SignupScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSignupSuccess = {
                    navController.navigate(Screen.Beranda.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(route = Screen.CompleteProfile.route) {
            CompleteProfileScreen(
                onProfileComplete = {
                    navController.navigate(Screen.Beranda.route) {
                        popUpTo(Screen.CompleteProfile.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main App Screens
        composable(route = Screen.Beranda.route) {
            ReVoiceScreen()
        }
        
        composable(route = Screen.Laporan.route) {
            com.tunawicara.app.presentation.laporan.LaporanScreen()
        }
        
        composable(route = Screen.Trofi.route) {
            com.tunawicara.app.presentation.trofi.TrofiScreen()
        }
        
        composable(route = Screen.Profil.route) {
            com.tunawicara.app.presentation.profil.ProfilScreen()
        }
    }
}

/**
 * Screen routes
 */
sealed class Screen(val route: String) {
    // Auth Screens
    object Login : Screen("login")
    object Signup : Screen("signup")
    object ForgotPassword : Screen("forgot_password")
    object CompleteProfile : Screen("complete_profile")
    
    // Main Screens
    object Beranda : Screen("beranda")
    object Laporan : Screen("laporan")
    object Trofi : Screen("trofi")
    object Profil : Screen("profil")
    object ExerciseDetail : Screen("exercise_detail")
    object Progress : Screen("progress")
}
