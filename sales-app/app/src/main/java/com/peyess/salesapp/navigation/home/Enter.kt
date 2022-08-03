package com.peyess.salesapp.navigation.home

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun homeEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        when (SalesAppScreens.fromRoute(initialState.destination.route)) {
            SalesAppScreens.Home ->
                fadeIn()
            SalesAppScreens.AddMeasure,
            SalesAppScreens.AddAngle,
            SalesAppScreens.MeasurePicture ->
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(transitionDuration),
                )
            else ->
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(transitionDuration),
                )
        }
}