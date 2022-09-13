package com.peyess.salesapp.navigation.sale.prescription.data

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun prescriptionDataScreenExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
                SalesAppScreens.FramesLanding -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(transitionDuration),
                )
                SalesAppScreens.SalePrescriptionDataSymptoms -> slideOutVertically (
                        targetOffsetY = { -it },
                        animationSpec = tween(transitionDuration),
                )
                else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(transitionDuration),
                )
        }
}

@OptIn(ExperimentalAnimationApi::class)
fun prescriptionDataSymptomsScreenExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutVertically (
                targetOffsetY = { -it },
                animationSpec = tween(transitionDuration),
        )
}