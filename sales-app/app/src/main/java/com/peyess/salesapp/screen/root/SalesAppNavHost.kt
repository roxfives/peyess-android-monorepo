package com.peyess.salesapp.screen.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.home.buildSalesAppNavGraph

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SalesAppNavHost(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    navHostController: NavHostController,
) {
    AnimatedNavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = SalesAppScreens.Home.name,
    ) {
        buildSalesAppNavGraph(
            navHostController = navHostController,
            builder = this,
        )
    }
}