package com.peyess.salesapp.navigation.sale.lens_pick.comparison

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun lensComparisonExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
                SalesAppScreens.ClientList -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(transitionDuration),
                )
                else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(transitionDuration),
                )
        }
}