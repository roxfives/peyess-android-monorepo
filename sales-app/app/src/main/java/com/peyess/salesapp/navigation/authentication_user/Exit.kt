package com.peyess.salesapp.navigation.authentication_user

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun userListExitTransition(transitionDuration: Int = 500): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? =
    {
        slideOutHorizontally (
            targetOffsetX = { -it },
            animationSpec = tween(transitionDuration)
        )
    }

@OptIn(ExperimentalAnimationApi::class)
fun userAuthExitTransition(transitionDuration: Int = 500): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? =
    {
        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
            SalesAppScreens.Home ->
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(transitionDuration),
                )
            else -> slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration),
            )
        }
    }

@OptIn(ExperimentalAnimationApi::class)
fun localPasscodeExitTransition(transitionDuration: Int = 500): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? =
    {

        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
            SalesAppScreens.Home ->
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(transitionDuration),
                )
            else ->
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(transitionDuration),
                )
        }
    }