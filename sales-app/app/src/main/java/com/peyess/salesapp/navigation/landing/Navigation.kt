package com.peyess.salesapp.navigation.landing

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.landing.Landing

@OptIn(ExperimentalAnimationApi::class)
fun buildLandingNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.Landing.name,
        enterTransition = landingAuthenticationEnterTransition(),
        exitTransition = landingAuthenticationExitTransition()
    ) {
        Landing(modifier = modifier, onTimeout =  {})
    }
}