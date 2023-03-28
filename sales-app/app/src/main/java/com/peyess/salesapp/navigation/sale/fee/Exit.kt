package com.peyess.salesapp.navigation.sale.fee

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun paymentFeeExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration),
        )
}