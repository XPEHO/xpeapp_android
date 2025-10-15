package com.xpeho.xpeapp.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.domain.AuthState
import com.xpeho.xpeapp.enums.Screens

// Navigation animation duration in milliseconds.
// The value of 300 ms provides a more fluid navigation experience,
// giving the illusion of better performance.
private const val NAV_ANIM_DURATION_MILLIS = 300

@Composable
fun Home(startScreen: Screens) {
    val navigationController = rememberNavController()

    // Observe the authentication state to handle navigation accordingly.
    val authState = XpeApp.appModule.authenticationManager.authState.collectAsStateWithLifecycle()
    
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated && 
            navigationController.currentDestination?.route != Screens.Login.name) {
            navigationController.navigate(Screens.Login.name) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navigationController,
        startDestination = startScreen.name,
        enterTransition = { fadeIn(animationSpec = tween(NAV_ANIM_DURATION_MILLIS)) },
        exitTransition = { fadeOut(animationSpec = tween(NAV_ANIM_DURATION_MILLIS)) }
    ) {
        navigationBuilder(navigationController)
    }
}
