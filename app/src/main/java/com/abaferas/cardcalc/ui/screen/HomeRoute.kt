package com.abaferas.cardcalc.ui.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.addHomeRoute() {
    composable(
        route = "home",
    ) {
        ScreenHome()
    }
}

fun NavController.navigateToHome() {
    navigate("home")
}