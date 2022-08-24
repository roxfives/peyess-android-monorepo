package com.peyess.salesapp.feature.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.bottom_bar.BottomBar
import com.peyess.salesapp.ui.component.bottom_bar.BottomNavBarActions
import com.peyess.salesapp.ui.component.top_bar.TopBar
import timber.log.Timber

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
        override val onHomeSelected: () -> Unit
            get() = {}
        override val onPeopleSelected: () -> Unit
            get() = {}
        override val onFramesSelected: () -> Unit
            get() = {}
        override val onRealMeasureSelected: () -> Unit
            get() = {}

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
                currentScreen = currentScreen,
                navHostController = navHostController,
                actions = actions,
            )
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            SalesAppNavHost(
                scaffoldState = scaffoldState,
                navHostController = navHostController,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}