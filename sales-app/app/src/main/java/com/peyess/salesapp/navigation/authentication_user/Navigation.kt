package com.peyess.salesapp.navigation.authentication_user

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.authentication_store.AuthScreen
import com.peyess.salesapp.screen.authentication_user.UserAuthScreen

@OptIn(ExperimentalAnimationApi::class)
fun buildUserAuthNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.UserAuthentication.name,
        enterTransition = userAuthenticationEnterTransition(),
        exitTransition = userAuthenticationExitTransition()
    ) {
        UserAuthScreen(modifier = modifier)
    }
}