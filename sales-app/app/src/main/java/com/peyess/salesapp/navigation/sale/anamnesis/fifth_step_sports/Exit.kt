package com.peyess.salesapp.navigation.sale.anamnesis.fifth_step_sports

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
fun fifthStepExitTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when (SalesAppScreens.fromRoute(targetState.destination.route)) {
                SalesAppScreens.AnamnesisSixthStep -> slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(transitionDuration),
                )
                else -> slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(transitionDuration),
                )
        }
}