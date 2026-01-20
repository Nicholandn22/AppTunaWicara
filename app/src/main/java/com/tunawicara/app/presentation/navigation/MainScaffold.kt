package com.tunawicara.app.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tunawicara.app.presentation.auth.AuthViewModel
import com.tunawicara.app.presentation.revoice.DarkText
import com.tunawicara.app.presentation.revoice.MintGreen
import com.tunawicara.app.presentation.revoice.TurquoiseBlue

/**
 * Main Scaffold with bottom navigation
 * Wraps all screens with shared bottom navigation bar
 */
@Composable
fun MainScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    
    // Check if current route is an auth screen
    val isAuthScreen = currentRoute in listOf(
        Screen.Login.route,
        Screen.Signup.route,
        Screen.ForgotPassword.route,
        Screen.CompleteProfile.route
    )
    
    // Check if current route is a main app screen (where we should show nav bars)
    val isMainScreen = currentRoute in listOf(
        Screen.Beranda.route,
        Screen.Laporan.route,
        Screen.Trofi.route,
        Screen.Profil.route
    )
    
    // Determine start destination based on login state
    val startDestination = if (isLoggedIn) Screen.Beranda.route else Screen.Login.route
    
    Scaffold(
        topBar = {
            // Show top bar only on main screens
            if (isMainScreen) {
                AppTopBar(
                    currentRoute = currentRoute,
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        },
        bottomBar = {
            // Show bottom nav only on main screens
            if (isMainScreen) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavGraph(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}

/**
 * App top bar with dynamic title based on current screen
 */
@Composable
fun AppTopBar(
    currentRoute: String?,
    onLogout: () -> Unit = {}
) {
    val title = when (currentRoute) {
        Screen.Beranda.route -> "ReVoice"
        Screen.Laporan.route -> "Laporan"
        Screen.Trofi.route -> "Trofi"
        Screen.Profil.route -> "Profil"
        else -> "ReVoice"
    }
    
    Column {
        // Main header
        Surface(
            color = TurquoiseBlue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                
                // Logout button
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint = DarkText
                    )
                }
            }
        }
    }
}

/**
 * Bottom navigation bar with 4 tabs and center FAB
 */
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Beranda
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Beranda",
                isSelected = currentRoute == Screen.Beranda.route,
                onClick = {
                    navController.navigate(Screen.Beranda.route) {
                        popUpTo(Screen.Beranda.route) { inclusive = true }
                    }
                }
            )
            
            // Laporan
            BottomNavItem(
                icon = Icons.Default.BarChart,
                label = "Laporan",
                isSelected = currentRoute == Screen.Laporan.route,
                onClick = {
                    navController.navigate(Screen.Laporan.route) {
                        popUpTo(Screen.Beranda.route)
                    }
                }
            )
            
            // Center Mic Button (Elevated)
            FloatingActionButton(
                onClick = {
                    // Navigate to Beranda if not already there
                    if (currentRoute != Screen.Beranda.route) {
                        navController.navigate(Screen.Beranda.route) {
                            popUpTo(Screen.Beranda.route) { inclusive = true }
                        }
                    }
                },
                containerColor = MintGreen,
                contentColor = DarkText,
                modifier = Modifier
                    .size(40.dp)
                    .offset(y = (-16).dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Record",
                    modifier = Modifier.size(32.dp)
                )
            }
            
            // Trofi
            BottomNavItem(
                icon = Icons.Default.EmojiEvents,
                label = "Trofi",
                isSelected = currentRoute == Screen.Trofi.route,
                onClick = {
                    navController.navigate(Screen.Trofi.route) {
                        popUpTo(Screen.Beranda.route)
                    }
                }
            )
            
            // Profil
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profil",
                isSelected = currentRoute == Screen.Profil.route,
                onClick = {
                    navController.navigate(Screen.Profil.route) {
                        popUpTo(Screen.Beranda.route)
                    }
                }
            )
        }
    }
}

/**
 * Individual bottom navigation item
 */
@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(64.dp)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) TurquoiseBlue else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            color = if (isSelected) TurquoiseBlue else Color.Gray,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
