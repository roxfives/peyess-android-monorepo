package com.peyess.salesapp.ui.component.top_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.Icon
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
import com.peyess.salesapp.ui.component.bottom_bar.shouldShowBottomBarFor

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
            title = { TopBarTitle() },
            navigationIcon = { NavigationIcon(canNavigateBack = canNavigateBack) }
        )
    }
}

@Composable
fun NavigationIcon(canNavigateBack: Boolean = true) {
    if (canNavigateBack) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.desc_navigate_back)
        )
    }
}

@Composable
fun TopBarTitle() {
    Text(stringResource(id = R.string.title))
}

fun shouldShowTopBarFor(
    screen: SalesAppScreens,
    navHostController: NavHostController,
): Boolean {
    val prevScreen =
        SalesAppScreens.fromRoute(navHostController.previousBackStackEntry?.destination?.route)

    return false//screen == SalesAppScreens.Home
}
