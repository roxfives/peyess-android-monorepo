package com.peyess.salesapp.navigation.sale.lens_pick.suggestion

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun lensSuggestionEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        scaleIn()
}