package com.tunawicara.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tunawicara.app.presentation.navigation.MainScaffold
import com.tunawicara.app.presentation.theme.TunaWicaraTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for Tuna Wicara app
 * Entry point for the Jetpack Compose UI
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TunaWicaraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainScaffold(navController = navController)
                }
            }
        }
    }
}
