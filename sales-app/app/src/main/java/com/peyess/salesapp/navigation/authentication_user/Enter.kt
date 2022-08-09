package com.peyess.salesapp.navigation.authentication_user

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun userListEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
    scaleIn()
}

@OptIn(ExperimentalAnimationApi::class)
fun userAuthEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
    scaleIn()
}

@OptIn(ExperimentalAnimationApi::class)
fun localPasscodeEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {

    when (SalesAppScreens.fromRoute(targetState.destination.route)) {
        SalesAppScreens.UserAuth ->
            slideInHorizontally { -it }
        else ->
            scaleIn()
    }
}