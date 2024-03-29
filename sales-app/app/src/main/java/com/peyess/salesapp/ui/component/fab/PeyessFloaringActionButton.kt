package com.peyess.salesapp.ui.component.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peyess.salesapp.navigation.SalesAppScreens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PeyessFloatingActionButton(
    modifier: Modifier = Modifier,
    currentScreen: SalesAppScreens = SalesAppScreens.Home,
    navHostController: NavHostController = rememberNavController(),
    onClick: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = shouldShowFabFor(currentScreen, navHostController),
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        FloatingActionButton(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            onClick = onClick,
        ) {
            Icon(imageVector = Icons.Filled.ViewModule, contentDescription = "")
        }
    }
}

fun shouldShowFabFor(
    screen: SalesAppScreens,
    navHostController: NavHostController
): Boolean {
    val prevScreen =
        SalesAppScreens.fromRoute(navHostController.previousBackStackEntry?.destination?.route)

    return screen == SalesAppScreens.LensComparison
}