package com.peyess.salesapp.navigation.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun homeExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
    slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(transitionDuration)
    )
}

@OptIn(ExperimentalAnimationApi::class)
fun saleExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
    when (SalesAppScreens.fromRoute(targetState.destination.route)) {
        SalesAppScreens.Home ->
            slideOutHorizontally (
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration),
            )
        SalesAppScreens.Clients ->
            slideOutHorizontally (
                targetOffsetX = { -it },
                animationSpec = tween(transitionDuration),
            )
        else ->
            slideOutVertically (
                targetOffsetY = { it },
                animationSpec = tween(transitionDuration),
            )
    }
}



@OptIn(ExperimentalAnimationApi::class)
fun clientsExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
    when (SalesAppScreens.fromRoute(targetState.destination.route)) {
        SalesAppScreens.Home,
        SalesAppScreens.SaleScreen ->
            slideOutHorizontally (
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration),
            )
        else ->
            slideOutHorizontally (
                targetOffsetX = { -it },
                animationSpec = tween(transitionDuration),
            )
    }
}