package com.peyess.salesapp.navigation.sale.payment

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun paymentExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        scaleOut()
}