package com.tunawicara.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tunawicara.app.presentation.revoice.ReVoiceScreen

/**
 * Navigation graph for the app
 */
@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Beranda.route
    ) {
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
        
        // Add more screens here as needed
        // composable(route = Screen.ExerciseDetail.route + "/{exerciseId}") { ... }
        // composable(route = Screen.Progress.route) { ... }
    }
}

/**
 * Screen routes
 */
sealed class Screen(val route: String) {
    object Beranda : Screen("beranda")
    object Laporan : Screen("laporan")
    object Trofi : Screen("trofi")
    object Profil : Screen("profil")
    object ExerciseDetail : Screen("exercise_detail")
    object Progress : Screen("progress")
}
