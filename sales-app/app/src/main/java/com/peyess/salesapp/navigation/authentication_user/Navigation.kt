package com.peyess.salesapp.navigation.authentication_user

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.authentication_user.screen.authentication.UserAuthScreen
import com.peyess.salesapp.feature.authentication_user.screen.local_password.LocalPasswordScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.authentication_user.screen.user_list.UserListAuthScreen

@OptIn(ExperimentalAnimationApi::class)
fun buildUserAuthNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.UserListAuthentication.name,
        enterTransition = userListEnterTransition(),
        exitTransition = userListExitTransition()
    ) {
        UserListAuthScreen(modifier = modifier, navHostController = navHostController)
    }

    builder.composable(
        route = SalesAppScreens.UserAuth.name,
        enterTransition = userAuthEnterTransition(),
        exitTransition = userAuthExitTransition()
    ) {
        UserAuthScreen(modifier = modifier, navHostController = navHostController)
    }

    builder.composable(
        route = SalesAppScreens.LocalPasscode.name,
        enterTransition = localPasscodeEnterTransition(),
        exitTransition = localPasscodeExitTransition()
    ) {
        LocalPasswordScreen(modifier = modifier, navHostController = navHostController)
    }
}