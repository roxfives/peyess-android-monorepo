package com.peyess.salesapp.navigation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.home.Home

@OptIn(ExperimentalAnimationApi::class)
fun buildSalesAppNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.Home.name,
        enterTransition = homeEnterTransition(),
        exitTransition = homeExitTransition()
    ) {
        Home(modifier = modifier, navHostController = navHostController)
    }
}