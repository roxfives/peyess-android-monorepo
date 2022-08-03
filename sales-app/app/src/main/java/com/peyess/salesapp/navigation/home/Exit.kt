package com.peyess.salesapp.navigation.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

const val transitionDuration = 500

@OptIn(ExperimentalAnimationApi::class)
fun homeExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
            SalesAppScreens.People,
            SalesAppScreens.Frames,
            SalesAppScreens.RealMeasures ->
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(transitionDuration)
                )

            else ->
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(transitionDuration)
                )
        }
}