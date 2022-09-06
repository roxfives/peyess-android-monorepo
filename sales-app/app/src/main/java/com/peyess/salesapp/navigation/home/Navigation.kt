package com.peyess.salesapp.navigation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.home.ClientScreen
import com.peyess.salesapp.feature.home.HomeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.home.SalesScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildHomeNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.Home.name,
        enterTransition = homeEnterTransition(),
        exitTransition = homeExitTransition()
    ) {
        HomeScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
        )
    }

    builder.composable(
        route = SalesAppScreens.SaleScreen.name,
        enterTransition = homeEnterTransition(),
        exitTransition = homeExitTransition()
    ) {
        SalesScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
        ) {
            navHostController.navigate(SalesAppScreens.SaleWelcome.name)
        }
    }
    builder.composable(
        route = SalesAppScreens.Clients.name,
        enterTransition = homeEnterTransition(),
        exitTransition = homeExitTransition()
    ) {
        ClientScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
        )
    }
}