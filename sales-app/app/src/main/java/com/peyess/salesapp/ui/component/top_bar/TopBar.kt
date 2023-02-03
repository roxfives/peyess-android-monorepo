package com.peyess.salesapp.ui.component.top_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.peyess.salesapp.R
import com.peyess.salesapp.navigation.SalesAppScreens

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean = false,
    currentScreen: SalesAppScreens,
    navHostController: NavHostController,
) {
    AnimatedVisibility(
        visible = shouldShowTopBarFor(currentScreen, navHostController),
        enter = slideInVertically(initialOffsetY = { 0 }),
        exit = slideOutVertically(targetOffsetY = { 0 }),
    ) {
        TopAppBar(
            modifier = modifier,
            title = { TopBarTitle(currentScreen) },
            navigationIcon = {
                NavigationIcon(
                    canNavigateBack = showNavigateBack(currentScreen, navHostController),
                    navHostController = navHostController,
                )
            }
        )
    }
}

@Composable
fun NavigationIcon(
    canNavigateBack: Boolean = false,
    navHostController: NavHostController,
) {
    if (canNavigateBack) {
        IconButton(onClick = { navHostController.popBackStack() }) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.desc_navigate_back)
            )
        }
    }
}

@Composable
fun TopBarTitle(screen: SalesAppScreens) {
    Text(SalesAppScreens.title(screen.name))
}

fun shouldShowTopBarFor(
    screen: SalesAppScreens,
    navHostController: NavHostController,
): Boolean {
    val prevScreen =
        SalesAppScreens.fromRoute(navHostController.previousBackStackEntry?.destination?.route)

    return !(screen == SalesAppScreens.UserListAuthentication
            || screen == SalesAppScreens.StoreAuthentication
            || screen == SalesAppScreens.FramesMeasureTakePicture
            || screen == SalesAppScreens.FramesMeasure
            || screen == SalesAppScreens.Landing
        )
}

fun showNavigateBack(
    screen: SalesAppScreens,
    navHostController: NavHostController,
): Boolean {
    val prevScreen =
        SalesAppScreens.fromRoute(navHostController.previousBackStackEntry?.destination?.route)

    return !(
        screen == SalesAppScreens.UserListAuthentication
            || screen == SalesAppScreens.SaleWelcome
            || screen == SalesAppScreens.SalePayment
            || screen == SalesAppScreens.SaleScreen
            || screen == SalesAppScreens.FramesMeasureTakePicture
            || screen == SalesAppScreens.FramesMeasure
            || screen == SalesAppScreens.Home
            || screen == SalesAppScreens.Clients
            || screen == SalesAppScreens.Landing
            || screen == SalesAppScreens.SetFramesData
    )
}
