package com.peyess.salesapp.navigation.pick_client

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.landing.Landing
import com.peyess.salesapp.feature.sale.pick_client.PickClientScreen

@OptIn(ExperimentalAnimationApi::class)
fun buildPickClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.PickClient.name,
        enterTransition = pickclientEnterTransition(),
        exitTransition = pickclientExitTransition()
    ) {
        PickClientScreen(modifier) {
            navHostController.navigate(SalesAppScreens.ServiceOrder.name)
        }
    }
}