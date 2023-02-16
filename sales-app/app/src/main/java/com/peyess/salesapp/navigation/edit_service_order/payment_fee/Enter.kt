package com.peyess.salesapp.navigation.edit_service_order.payment_fee

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
fun editPaymentFeeEnterTransition(transitionDuration: Int = 500):
        (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
    when (SalesAppScreens.fromRoute(initialState.destination.route)) {
        SalesAppScreens.PickClient,
        SalesAppScreens.LensComparison,
        SalesAppScreens.SalePrescriptionData ->
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(transitionDuration),
            )
        else ->
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(transitionDuration),
            )
    }
}