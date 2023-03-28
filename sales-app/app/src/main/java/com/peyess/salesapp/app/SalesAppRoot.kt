package com.peyess.salesapp.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.peyess.salesapp.app.navigation.SalesAppNavHost
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.bottom_bar.BottomBar
import com.peyess.salesapp.ui.component.bottom_bar.BottomNavBarActions
import com.peyess.salesapp.ui.component.fab.PeyessFloatingActionButton
import com.peyess.salesapp.ui.component.top_bar.TopBar
import timber.log.Timber

val bottomBarHeight = 72.dp

@Composable
fun SalesAppRoot(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val backstackEntry = navHostController.currentBackStackEntryAsState()
    val currentScreen = SalesAppScreens.fromRoute(
        backstackEntry.value?.destination?.route
    )
    Timber.i("Current screen: $currentScreen")

    val scaffoldState = rememberScaffoldState()

    val actions = object: BottomNavBarActions {
        override val onHomeSelected = {
//            navHostController.backQueue.clear()
            navHostController.navigate(SalesAppScreens.Home.name)
        }
        override val onPeopleSelected = {
//            navHostController.backQueue.clear()
            navHostController.navigate(SalesAppScreens.Clients.name)
        }
        override val onSaleSelected = {
//            navHostController.backQueue.clear()
            navHostController.navigate(SalesAppScreens.SaleScreen.name)
        }

    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,

        topBar = {
            TopBar(
                currentScreen = currentScreen,
                navHostController = navHostController,
            )
        },
        bottomBar = {
            BottomBar(
                modifier = Modifier.height(72.dp),
                currentScreen = currentScreen,
                navHostController = navHostController,
                actions = actions,
            )
        },

        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            PeyessFloatingActionButton(
                currentScreen = currentScreen,
                navHostController = navHostController,
                onClick = {
                    navHostController.navigate(SalesAppScreens.Demonstration.name)
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.padding(it).fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            SalesAppNavHost(
                scaffoldState = scaffoldState,
                navHostController = navHostController,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}