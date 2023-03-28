package com.peyess.salesapp.navigation.sale.welcome

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.sale.welcome.WelcomeScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildWelcomeNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.SaleWelcome.name,
        enterTransition = welcomeEnterTransition(),
        exitTransition = welcomeExitTransition()
    ) {
        WelcomeScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.screen_offset),
            onCancelSale = { navHostController.popBackStack() },
            onNext = { navHostController.navigate(SalesAppScreens.SalePrescriptionLensType.name) },
        ) 
    }
}