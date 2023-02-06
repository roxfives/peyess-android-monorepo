package com.peyess.salesapp.screen.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.authentication.buildStoreAuthNavGraph
import com.peyess.salesapp.navigation.authentication_user.buildUserAuthNavGraph
import com.peyess.salesapp.navigation.create_client.buildCreateClientNavGraph
import com.peyess.salesapp.navigation.demonstration.buildDemonstrationNavGraph
import com.peyess.salesapp.navigation.home.buildHomeNavGraph
import com.peyess.salesapp.navigation.landing.buildLandingNavGraph
import com.peyess.salesapp.navigation.sale.buildSaleNavGraph
import com.peyess.salesapp.navigation.sale.discount.buildDiscountNavGraph
import com.peyess.salesapp.navigation.sale.fee.buildFeeNavGraph
import com.peyess.salesapp.navigation.settings_actions.buildSettingsAndActionsNavGraph
import com.peyess.salesapp.navigation.visual_acuity.buildVisualAcuityNavGraph

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
        startDestination = SalesAppScreens.Landing.name,
    ) {
        buildHomeNavGraph(
            navHostController = navHostController,
            builder = this,
        )

        buildStoreAuthNavGraph(
            navHostController = navHostController,
            builder = this,
        )

        buildLandingNavGraph(
            navHostController = navHostController,
            builder = this,
        )

        buildUserAuthNavGraph(
            navHostController = navHostController,
            builder = this,
        )

        buildSaleNavGraph(modifier, navHostController, this)

        buildDemonstrationNavGraph(modifier, navHostController, this)

        buildCreateClientNavGraph(modifier, navHostController, this)

        buildVisualAcuityNavGraph(modifier, navHostController, this)

        buildSettingsAndActionsNavGraph(modifier, navHostController, this)

        buildDiscountNavGraph(modifier, navHostController, this)

        buildFeeNavGraph(modifier, navHostController, this)
    }
}