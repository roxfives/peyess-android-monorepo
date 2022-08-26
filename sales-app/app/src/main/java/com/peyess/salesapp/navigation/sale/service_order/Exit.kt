package com.peyess.salesapp.navigation.sale.service_order

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleOut
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun serviceOrderExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        scaleOut()
}