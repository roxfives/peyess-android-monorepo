package com.peyess.salesapp.navigation.sale.edit_service_order

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun editServiceOrderExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
    when (SalesAppScreens.fromRoute(targetState.destination.route)) {
        SalesAppScreens.PickClient -> slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(transitionDuration),
        )
        SalesAppScreens.Home -> slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(transitionDuration),
        )
        else -> slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(transitionDuration),
        )
    }
}