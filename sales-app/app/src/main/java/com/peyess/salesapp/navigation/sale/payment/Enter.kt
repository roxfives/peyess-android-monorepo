package com.peyess.salesapp.navigation.sale.payment

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun paymentEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        scaleIn()
}