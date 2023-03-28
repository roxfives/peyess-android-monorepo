package com.peyess.salesapp.ui.component.bottom_bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.peyess.salesapp.R
import com.peyess.salesapp.navigation.SalesAppScreens

private val buttonPadding = 1.dp

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
            modifier = modifier,
            elevation = 10.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(buttonPadding)
                    .weight(weight = 1f, fill = true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.Home)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                IconButton(onClick = { actions.onHomeSelected() }) {
                   Icon(Icons.Filled.Home, tint = tint, contentDescription = "")
                }

                Text(
                    text = stringResource(id = R.string.bottom_bar_home),
                    style = MaterialTheme.typography.caption.copy(color = tint),
                )
            }

            Column(
                modifier = Modifier
                    .padding(buttonPadding)
                    .weight(weight = 1f, fill = true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.SaleScreen)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                IconButton(onClick = { actions.onSaleSelected() }) {
                    Icon(Icons.Filled.LocalOffer, tint = tint, contentDescription = "")
                }

                Text(
                    text = stringResource(id = R.string.bottom_bar_sale),
                    style = MaterialTheme.typography.caption.copy(color = tint),
                )
            }

            Column(
                modifier = Modifier
                    .padding(buttonPadding)
                    .weight(weight = 1f, fill = true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val tint by animateColorAsState(
                    if (currentScreen == SalesAppScreens.Clients)
                        MaterialTheme.colors.onPrimary
                    else
                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
                )

                IconButton(onClick = { actions.onPeopleSelected() }) {
                    Icon(Icons.Filled.Person, tint = tint, contentDescription = "")
                }

                Text(
                    text = stringResource(id = R.string.bottom_bar_client),
                    style = MaterialTheme.typography.caption.copy(color = tint),
                )
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

    return screen == SalesAppScreens.Home
            || screen == SalesAppScreens.SaleScreen
            || screen == SalesAppScreens.Clients
}