package com.peyess.salesapp.navigation.authentication_user

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun userAuthenticationExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        scaleOut()
}