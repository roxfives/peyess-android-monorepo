package com.peyess.salesapp.ui.component.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.SalesAppScreens

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    currentScreen: SalesAppScreens,
    navHostController: NavHostController,
    actions: BottomNavBarActions,
) {
    AnimatedVisibility(
        visible = shouldShowBottomBarFor(currentScreen, navHostController),
        enter = slideInVertically(initialOffsetY = { 0 }),
        exit = slideOutVertically(targetOffsetY = { 0 }),
    ) {
        BottomAppBar(
            elevation = 10.dp,
            modifier = modifier
        ) {
            IconButton(
                onClick = {
                    actions.onHomeSelected()
                },
                modifier = Modifier.weight(1f, true),
            ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.Home)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                Icon(Icons.Filled.Home, tint = tint, contentDescription = "")
            }

            IconButton(
                onClick = {
                    actions.onPeopleSelected()
                },
                modifier = Modifier.weight(1f, true),

                ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.People)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                Icon(Icons.Filled.Person, tint = tint, contentDescription = "")
            }

            IconButton(
                onClick = { actions.onPeopleSelected() },
                modifier = Modifier.weight(1f, true),
            ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.People)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                Icon(Icons.Filled.Person, tint = tint, contentDescription = "")
            }
        }
    }
}

fun shouldShowBottomBarFor(
    screen: SalesAppScreens,
    navHostController: NavHostController
): Boolean {
    val prevScreen =
        SalesAppScreens.fromRoute(navHostController.previousBackStackEntry?.destination?.route)

    return true//screen == SalesAppScreens.Home
}