package com.peyess.salesapp.navigation.authentication

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.authentication.AuthScreen

@OptIn(ExperimentalAnimationApi::class)
fun buildStoreAuthNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.StoreAuthentication.name,
        enterTransition = storeAuthenticationEnterTransition(),
        exitTransition = storeAuthenticationExitTransition()
    ) {
        AuthScreen(modifier = modifier)
    }
}